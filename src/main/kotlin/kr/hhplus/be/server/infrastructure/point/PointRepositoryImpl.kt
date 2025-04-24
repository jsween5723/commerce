package kr.hhplus.be.server.infrastructure.point

import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.point.PointRepository
import kr.hhplus.be.server.domain.point.UserPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*

@Repository
class PointRepositoryImpl(private val pointJpaRepository: PointJpaRepository) : PointRepository {
    override fun findByUserId(userId: UserId): UserPoint {
        return pointJpaRepository.findById(userId.userId).orElseGet {
            pointJpaRepository.saveAndFlush(
                UserPoint(userId.userId, BigDecimal.ZERO)
            )
        }
    }

    override fun save(point: UserPoint): UserPoint {
        return pointJpaRepository.save(point)
    }
}

interface PointJpaRepository : JpaRepository<UserPoint, Long> {
    override fun findById(userId: Long): Optional<UserPoint>
}