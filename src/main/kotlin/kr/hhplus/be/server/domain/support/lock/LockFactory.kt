package kr.hhplus.be.server.domain.support.lock

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

interface LockFactory {
    fun create(key: String, maxTime: Long, unit: TimeUnit, type: LockType): Lock
}