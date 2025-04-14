package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.auth.UserId
import org.springframework.stereotype.Repository

@Repository
interface PointRepository {
    fun findByUserId(userId: UserId): UserPoint
}