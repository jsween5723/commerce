package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.point.PointCommand
import kr.hhplus.be.server.domain.point.PointException
import java.math.BigDecimal

class PointCriteria {
    data class Charge(val amount: BigDecimal, val authentication: Authentication) {
        init {
            if (amount < BigDecimal.ZERO) throw PointException.MinusAmountCantApply()
        }

        fun toChargeCommand() = PointCommand.Charge(amount, authentication.id, authentication)
    }
}
