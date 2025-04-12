package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.auth.Authentication
import java.math.BigDecimal

class ChargeFixture(
    amount: BigDecimal = BigDecimal.valueOf(100),
    authentication: Authentication = Authentication(1L),
) {
    val charge = PointCommand.Charge(amount, authentication.id, authentication)
}

class UseFixture(
    amount: BigDecimal = BigDecimal.valueOf(100),
    authentication: Authentication = Authentication(1L),
) {
    val charge = PointCommand.Use(amount, authentication.id, authentication)
}