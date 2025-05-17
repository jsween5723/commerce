package kr.hhplus.be.server.domain.support.lock

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

interface LockRepository {
    fun getLock(
        key: String,
        lease: Long = 3000L,
        maxTime: Long = 5000L,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        type: LockType = LockType.REDIS_SPIN
    ): Lock
}