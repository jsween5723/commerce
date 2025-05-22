package kr.hhplus.be.server.domain.payment

import java.math.BigDecimal

class PaymentInfo {
    data class Cancel(val pointAmount: BigDecimal) {
        val hasAmount: Boolean = pointAmount != BigDecimal.ZERO
    }

    data class Pay(val pointAmount: BigDecimal)
}