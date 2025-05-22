package kr.hhplus.be.server.interfaces.order.event

import kr.hhplus.be.server.domain.event.OrderEvent
import kr.hhplus.be.server.domain.event.OrderEventPublisher
import kr.hhplus.be.server.domain.event.PaymentEvent
import kr.hhplus.be.server.domain.order.OrderService
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderPaymentEventListener(
    private val orderService: OrderService,
    private val orderEventPublisher: OrderEventPublisher
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: PaymentEvent.OrderPaymentCompleted) {
        val order = orderService.complete(orderId = event.orderId, authentication = event.authentication)
        orderEventPublisher.complete(OrderEvent.Complete(order = order))
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: PaymentEvent.OrderPaymentCreated) {
        orderService.applyPayment(event.orderId, event.paymentId)
    }
}