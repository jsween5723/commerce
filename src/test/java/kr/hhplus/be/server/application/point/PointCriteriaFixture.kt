package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.auth.Authentication
import org.mockito.Mockito.mock
import java.math.BigDecimal

class ChargeFixture(
    amount: BigDecimal = BigDecimal.valueOf(100),
    authentication: Authentication = mock(Authentication::class.java),
) {
    val charge = PointCriteria.Charge(amount, authentication)
}