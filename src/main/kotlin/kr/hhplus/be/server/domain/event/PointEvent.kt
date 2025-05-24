package kr.hhplus.be.server.domain.event

import kr.hhplus.be.server.domain.auth.Authentication
import java.math.BigDecimal

class PointEvent {
    data class PaymentOrderUse(val orderId: Long, val amount: BigDecimal, val authentication: Authentication)
}