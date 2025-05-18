package kr.hhplus.be.server.infrastructure.lock

import kr.hhplus.be.server.domain.support.lock.LockFactory
import kr.hhplus.be.server.domain.support.lock.LockType
import org.redisson.api.RedissonClient
import org.redisson.api.options.CommonOptions
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock


@Component
class RedisLockFactory(val redisTemplate: StringRedisTemplate, val reddisonClient: RedissonClient) : LockFactory {

    override fun create(key: String, maxTime: Long, unit: TimeUnit, type: LockType): Lock {
        return when (type) {
            LockType.REDIS_SPIN ->
                SpinLock(key = key, maxTime = maxTime, unit = unit, redisTemplate = redisTemplate)

            LockType.REDIS_PUBSUB ->
                reddisonClient.getLock(CommonOptions.name(key).timeout(Duration.of(maxTime, unit.toChronoUnit())))
        }
    }
}

private class SpinLock(
    private val key: String,
    private val redisTemplate: StringRedisTemplate,
    private val lease: Long = 3000L,
    private val maxTime: Long = 5000L,
    private val unit: TimeUnit = TimeUnit.MILLISECONDS,
) : Lock {
    val name: String = key
    private val value: String = UUID.randomUUID().toString()

    init {
        if (names.get().contains(key)) throw IllegalStateException("Key $key is already in use")
        names.get().add(key)
    }

    override fun lock() {
        throw NotImplementedError()
    }

    override fun lockInterruptibly() {
        throw NotImplementedError()
    }

    override fun tryLock(): Boolean {
        return tryLock(lease, unit)
    }

    override fun tryLock(lease: Long, unit: TimeUnit): Boolean {
        val startTime = System.currentTimeMillis()
        while (redisTemplate.opsForValue().setIfAbsent(key, value, lease, unit) != true) {
            if (isTimeout(startTime, unit)) {
                return false
            }
            Thread.onSpinWait()
        }
        return true
    }

    private fun isTimeout(startTime: Long, unit: TimeUnit): Boolean =
        System.currentTimeMillis() - startTime >= unit.toMillis(maxTime)


    override fun unlock() {
        redisTemplate.execute {
            DefaultRedisScript<Long>(UNLOCK_SCRIPT)
            listOf(key)
            value
        }
        names.get().remove(key)
    }

    override fun newCondition(): Condition {
        throw NotImplementedError()
    }

    companion object {
        private val UNLOCK_SCRIPT = """
        if redis.call("get", KEYS[1]) == ARGV[1] then
            return redis.call("del", KEYS[1])
        else
            return 0
        end
    
    """.trimIndent()
        private val names: ThreadLocal<MutableSet<String>> = ThreadLocal.withInitial { mutableSetOf() }
    }
}