package kr.hhplus.be.server.domain.support.lock

import java.util.concurrent.TimeUnit

annotation class DistributedLock(
    val key: String,
    val domain: LockKey,
    val waitTime: Long = 5000L,
    val lease: Long = 3000L,
    val unit: TimeUnit = TimeUnit.MILLISECONDS,
    val type: LockType = LockType.REDIS_SPIN,
)
