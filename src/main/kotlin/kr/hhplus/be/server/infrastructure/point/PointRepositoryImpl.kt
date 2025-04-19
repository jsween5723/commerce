package kr.hhplus.be.server.infrastructure.point

import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.point.PointRepository
import kr.hhplus.be.server.domain.point.UserPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class PointRepositoryImpl(private val pointJpaRepository: PointJpaRepository) : PointRepository {

    override fun findByUserId(userId: UserId): UserPoint {
        return pointJpaRepository.findByUserId(userId) ?: pointJpaRepository.save(UserPoint(userId, BigDecimal.ZERO))
    }
}

interface PointJpaRepository : JpaRepository<UserPoint, Long> {
    fun findByUserId(userId: UserId): UserPoint?
}