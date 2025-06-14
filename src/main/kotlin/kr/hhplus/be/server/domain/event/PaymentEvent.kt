package kr.hhplus.be.server.domain.event

import kr.hhplus.be.server.domain.auth.Authentication
import java.math.BigDecimal

class PaymentEvent {
    data class OrderPaymentCompleted(val orderId: Long, val amount: BigDecimal, val authentication: Authentication)
    data class OrderPaymentCreated(val orderId: Long, val paymentId: Long, val authentication: Authentication)
}