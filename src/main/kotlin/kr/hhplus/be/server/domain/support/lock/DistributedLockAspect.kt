package kr.hhplus.be.server.domain.support.lock

import kr.hhplus.be.server.domain.support.transaction.hasTransaction
import kr.hhplus.be.server.domain.support.transaction.registerAfterTransactionCompletion
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class DistributedLockAspect(private val lockFactory: LockFactory) {
    @Around("@annotation(DistributedLock)")
    fun distributeLock(joinPoint: ProceedingJoinPoint): Any {
        val signature = joinPoint.signature as MethodSignature
        val annotation = signature.method.getAnnotation(DistributedLock::class.java)!!
        val lock = lockFactory.create(
            key = annotation.key,
            maxTime = annotation.waitTime,
            unit = annotation.unit,
            type = annotation.type
        )
        if (!lock.tryLock(annotation.lease, annotation.unit))
            throw IllegalStateException("락 획득 대기 시간 초과 : ${annotation.key}")
        try {
            return joinPoint.proceed()
        } finally {
            if (hasTransaction()) {
                registerAfterTransactionCompletion {
                    lock.unlock()
                }
            } else lock.unlock()
        }
    }
}