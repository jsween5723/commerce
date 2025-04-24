package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.auth.UserId
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointService(
    private val pointRepository: PointRepository
) {
    fun findByUserId(userId: UserId) = pointRepository.findByUserId(userId)

    @Transactional
    @Retryable
    fun charge(command: PointCommand.Charge): UserPoint {
        val (amount, userId, authentication) = command
        val point = pointRepository.findByUserId(userId = userId)
        point.charge(amount = amount, authentication)
        return point
    }

    @Transactional
    fun use(command: PointCommand.Use): UserPoint {
        val (amount, userId, authentication) = command
        val point = pointRepository.findByUserId(userId = userId)
        point.use(amount = amount, authentication)
        return point
    }
}