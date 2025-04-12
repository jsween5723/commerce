package kr.hhplus.be.server.domain.order.payment

import kr.hhplus.be.server.domain.auth.Authentication

class PaymentCommand {
    data class Cancel(val paymentId: Long, val authentication: Authentication)
    data class Pay(val paymentId: Long, val authentication: Authentication)
}
