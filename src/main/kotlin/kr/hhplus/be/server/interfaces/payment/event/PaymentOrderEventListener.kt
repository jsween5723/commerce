package kr.hhplus.be.server.interfaces.payment.event

import kr.hhplus.be.server.domain.event.OrderEvent
import kr.hhplus.be.server.domain.event.PaymentEvent
import kr.hhplus.be.server.domain.payment.PaymentEventPublisher
import kr.hhplus.be.server.domain.payment.PaymentService
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PaymentOrderEventListener(
    private val paymentService: PaymentService,
    private val eventPublisher: PaymentEventPublisher
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: OrderEvent.Created) {
        val payment = paymentService.create(amount = event.price, authentication = event.authentication)
        eventPublisher.orderPaymentCreated(
            PaymentEvent.OrderPaymentCreated(
                orderId = event.orderId,
                paymentId = payment.id,
                authentication = event.authentication,
            )
        )
    }
}