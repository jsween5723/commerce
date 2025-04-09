package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.auth.Authentication
import java.math.BigDecimal

class PointCommand {
    data class Charge(val amount: BigDecimal, val authentication: Authentication) {
        init {
            if (amount < BigDecimal.ZERO) throw PointException.MinusAmountCantApply()
        }
    }

    data class Use(val amount: BigDecimal, val authentication: Authentication) {
        init {
            if (amount < BigDecimal.ZERO) throw PointException.MinusAmountCantApply()
        }
    }
}
