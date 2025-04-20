package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.auth.UserId
import java.math.BigDecimal

fun UserPointFixture(
    userId: Long = 1L,
    point: BigDecimal = BigDecimal.valueOf(100),
) = UserPoint(
    userId = UserId(userId),
    point = point,
)