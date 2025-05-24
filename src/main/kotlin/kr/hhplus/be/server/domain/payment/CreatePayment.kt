package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import java.math.BigDecimal

class CreatePayment private constructor(val orderId: Long, val amount: BigDecimal, val userId: UserId) {
    companion object {
        fun from(paymentOrderVO: PaymentOrderVO, authentication: Authentication) = CreatePayment(
            orderId = paymentOrderVO.orderId,
            amount = paymentOrderVO.amount,
            userId = authentication.id
        )
    }
}
