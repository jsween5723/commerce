package kr.hhplus.be.server.infrastructure.point

import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.point.PointRepository
import kr.hhplus.be.server.domain.point.UserPoint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Repository
class PointRepositoryImpl(private val pointJpaRepository: PointJpaRepository) : PointRepository {

    @Transactional
    override fun findByUserId(userId: UserId): UserPoint {
        val findByUserId = pointJpaRepository.findByUserId(userId.userId)
        if (findByUserId.isEmpty()) return pointJpaRepository.save(
            UserPoint(
                userId = userId,
                point = BigDecimal.valueOf(0.00)
            )
        )
        println(findByUserId)
//        return pointJpaRepository.findByUserId(userId.userId) ?: pointJpaRepository.save(
//            UserPoint(
//                userId,
//                BigDecimal.ZERO
//            )
//        )
        return findByUserId[0]
    }
}

interface PointJpaRepository : JpaRepository<UserPoint, Long> {
    @Query("select up from user_points up where up.userId.userId = :userId")
    fun findByUserId(userId: Long): List<UserPoint>
}