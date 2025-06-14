package kr.hhplus.be.server.infrastructure.payment

import kr.hhplus.be.server.domain.event.PaymentEvent
import kr.hhplus.be.server.domain.payment.PaymentEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class PaymentSpringEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) :
    PaymentEventPublisher {
    override fun orderPaymentPaid(event: PaymentEvent.OrderPaymentCompleted) {
        applicationEventPublisher.publishEvent(event)
    }

    override fun orderPaymentCreated(event: PaymentEvent.OrderPaymentCreated) {
        applicationEventPublisher.publishEvent(event)
    }
}