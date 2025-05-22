package kr.hhplus.be.server.interfaces.payment.event

import kr.hhplus.be.server.domain.event.PaymentEvent
import kr.hhplus.be.server.domain.payment.PaymentEventPublisher
import kr.hhplus.be.server.domain.event.PointEvent
import kr.hhplus.be.server.domain.payment.PaymentService
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PaymentPointEventListener(
    private val paymentService: PaymentService,
    private val eventPublisher: PaymentEventPublisher
) {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: PointEvent.PaymentOrderUse) {
        paymentService.pay(event.orderId, event.authentication)
        eventPublisher.orderPaymentPaid(
            PaymentEvent.OrderPaymentCompleted(
                orderId = event.orderId,
                amount = event.amount,
                authentication = event.authentication,
            )
        )
    }
}