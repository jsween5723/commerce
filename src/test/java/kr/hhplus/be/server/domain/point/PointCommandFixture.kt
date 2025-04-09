package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.auth.Authentication
import org.mockito.Mockito.mock
import java.math.BigDecimal

class ChargeFixture(
    amount: BigDecimal = BigDecimal.valueOf(100),
    authentication: Authentication = mock(Authentication::class.java),
) {
    val charge = PointCommand.Charge(amount, authentication.userId, authentication)
}

class UseFixture(
    amount: BigDecimal = BigDecimal.valueOf(100),
    authentication: Authentication = mock(Authentication::class.java),
) {
    val charge = PointCommand.Use(amount, authentication.userId, authentication)
}