package kr.hhplus.be.server.domain.order.payment

import java.math.BigDecimal

class PaymentInfo {
    data class Cancel(val pointAmount: BigDecimal) {
        val hasAmount: Boolean = pointAmount != BigDecimal.ZERO
    }

    data class Pay(val pointAmount: BigDecimal)
}