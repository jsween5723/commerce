package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.auth.Authentication
import org.mockito.Mockito.mock

class ChargeFixture(
    amount: Long = 100,
    authentication: Authentication = mock(Authentication::class.java),
) {
    val charge = PointCommand.Charge(amount, authentication)
}

class UseFixture(
    amount: Long = 100,
    authentication: Authentication = mock(Authentication::class.java),
) {
    val charge = PointCommand.Use(amount, authentication)
}