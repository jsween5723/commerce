package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.event.PaymentEvent

interface PaymentEventPublisher {
    fun orderPaymentPaid(event: PaymentEvent.OrderPaymentCompleted)
    fun orderPaymentCreated(event: PaymentEvent.OrderPaymentCreated)
}