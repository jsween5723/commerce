package kr.hhplus.be.server.domain

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class LockTemplate {
    private val lockMap = ConcurrentHashMap<String, Lock>()
    fun <T> runWithLock(key: Any, action: () -> T): T {
        val lock = lockMap.computeIfAbsent(createKey(key)) { ReentrantLock(true) }
        return lock.withLock { action() }
    }

    abstract fun createKey(key: Any): String
}