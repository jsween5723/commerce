package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.auth.Authentication

class PointCommand {
    data class Charge(val amount: Long, val authentication: Authentication) {
        init {
            if (amount < 0) throw PointException.MinusAmountCantApply()
        }
    }

    data class Use(val amount: Long, val authentication: Authentication) {
        init {
            if (amount < 0) throw PointException.MinusAmountCantApply()
        }
    }
}
