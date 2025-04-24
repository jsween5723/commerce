package kr.hhplus.be.server.domain.point

import jakarta.persistence.OptimisticLockException
import kr.hhplus.be.server.domain.auth.UserId
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointService(
    private val pointRepository: PointRepository
) {
    fun findByUserId(userId: UserId) = pointRepository.findByUserId(userId)

    @Transactional
    @Retryable(
        retryFor = [OptimisticLockingFailureException::class, OptimisticLockException::class],
        maxAttempts = 1,
        recover = "pleaseTryAgain"
    )
    fun charge(command: PointCommand.Charge): UserPoint {
        val (amount, userId, authentication) = command
        val point = pointRepository.findByUserId(userId = userId)
        point.charge(amount = amount, authentication)
        return point
    }

    @Recover
    fun pleaseTryAgain(command: PointCommand.Charge): UserPoint {
        throw PointException.PleaseTryAgain()
    }

    @Transactional
    @Retryable(
        retryFor = [OptimisticLockingFailureException::class, OptimisticLockException::class],
        maxAttempts = 1,
        recover = "pleaseTryAgain"
    )
    fun use(command: PointCommand.Use): UserPoint {
        val (amount, userId, authentication) = command
        val point = pointRepository.findByUserId(userId = userId)
        point.use(amount = amount, authentication)
        return point
    }
}