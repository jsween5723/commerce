package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.auth.Authentication

class PaymentCommand {
    data class Cancel(val paymentId: Long, val authentication: Authentication)
}
