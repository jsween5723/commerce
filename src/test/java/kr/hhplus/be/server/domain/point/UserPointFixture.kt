package kr.hhplus.be.server.domain.point

import java.math.BigDecimal

fun UserPointFixture(
    userId: Long = 1L,
    point: BigDecimal = BigDecimal.valueOf(100),
) = UserPoint(
    userId = userId,
    point = point,
)