package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.auth.UserId

interface PointRepository {
    fun findByUserId(userId: UserId): UserPoint
}