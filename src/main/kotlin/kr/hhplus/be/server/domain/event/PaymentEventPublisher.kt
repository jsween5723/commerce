package kr.hhplus.be.server.domain.event

interface PaymentEventPublisher {
    fun orderPaymentPaid(event: PaymentEvent.OrderPaymentCompleted)
}