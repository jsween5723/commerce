package kr.hhplus.be.server.domain.point

class UserPointFixture(
    userId: Long = 1L,
    point: Long = 100,
) : UserPoint(
    userId = userId,
    point = point,
)