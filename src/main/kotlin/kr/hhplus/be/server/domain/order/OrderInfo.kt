package kr.hhplus.be.server.domain.order

import java.math.BigDecimal

class OrderInfo {
    data class Cancel(val pointAmount: BigDecimal) {
        val hasAmount: Boolean = pointAmount != BigDecimal.ZERO
    }

    data class Pay(val pointAmount: BigDecimal)
}