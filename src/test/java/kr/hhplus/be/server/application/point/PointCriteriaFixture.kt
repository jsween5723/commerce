package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.auth.Authentication
import org.mockito.Mockito.mock

class ChargeFixture(
    amount: Long = 100,
    authentication: Authentication = mock(Authentication::class.java),
) {
    val charge = PointCriteria.Charge(amount, authentication)
}