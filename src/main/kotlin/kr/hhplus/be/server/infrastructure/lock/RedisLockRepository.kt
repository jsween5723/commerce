package kr.hhplus.be.server.infrastructure.lock

import kr.hhplus.be.server.domain.support.lock.LockRepository
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
class RedisLockRepository(val redisTemplate: StringRedisTemplate, val reddisonClient: RedissonClient) : LockRepository {
    override fun tryAndGet(key: String, lease: Long, maxTime: Long, unit: TimeUnit, type: LockType): Lock {
        return when (type) {
            LockType.REDIS_SPIN -> {
                val lock = SpinLock(key, lease, maxTime, unit, redisTemplate)
                lock.tryLock()
                lock
            }

            LockType.REDIS_PUBSUB -> {
                val lock =
                    reddisonClient.getLock(CommonOptions.name(key).timeout(Duration.of(maxTime, unit.toChronoUnit())))
                lock.tryLock(lease, unit)
                lock
            }
        }
    }
}

private class SpinLock(
    private val key: String,
    private val lease: Long = 3000L,
    private val maxTime: Long = 5000L,
    private val unit: TimeUnit = TimeUnit.MILLISECONDS,
    private val redisTemplate: StringRedisTemplate
) : Lock {
    private val value: String = UUID.randomUUID().toString()
    override fun lock() {
        throw NotImplementedError()
    }

    override fun lockInterruptibly() {
        throw NotImplementedError()
    }

    override fun tryLock(): Boolean {
        return tryLock(lease, TimeUnit.MILLISECONDS)
    }

    override fun tryLock(lease: Long, unit: TimeUnit): Boolean {
        val startTime = System.currentTimeMillis()
        while (redisTemplate.opsForValue().setIfAbsent(key, value, lease, unit) != true) {
            if (isTimeout(startTime, unit)) {
                throw IllegalStateException("락 획득 대기 시간 초과 : $key")
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
    }
}