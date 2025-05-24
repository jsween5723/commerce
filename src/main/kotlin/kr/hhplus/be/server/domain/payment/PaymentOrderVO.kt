package kr.hhplus.be.server.domain.payment

import java.math.BigDecimal

data class PaymentOrderVO(val orderId: Long, val amount: BigDecimal)
