package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.point.PointCommand
import kr.hhplus.be.server.domain.point.PointException

class PointCriteria {
    data class Charge(val amount: Long, val authentication: Authentication) {
        init {
            if (amount < 0) throw PointException.MinusAmountCantApply()
        }

        fun toChargeCommand() = PointCommand.Charge(amount, authentication)
    }
}
