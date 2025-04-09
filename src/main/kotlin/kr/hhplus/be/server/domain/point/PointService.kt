package kr.hhplus.be.server.domain.point

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointService(
    private val pointRepository: PointRepository
) {

    @Transactional
    fun charge(command: PointCommand.Charge): UserPoint {
        val (amount, authentication) = command
        val point = pointRepository.findByUserId(userId = authentication.userId)
        point.charge(amount = amount)
        return point
    }

    @Transactional
    fun use(command: PointCommand.Use): UserPoint {
        val (amount, authentication) = command
        val point = pointRepository.findByUserId(userId = authentication.userId)
        point.use(amount = amount)
        return point
    }
}