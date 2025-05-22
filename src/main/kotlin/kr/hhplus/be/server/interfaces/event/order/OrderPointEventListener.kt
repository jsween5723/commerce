package kr.hhplus.be.server.interfaces.event.order

import kr.hhplus.be.server.domain.event.PointEvent
import kr.hhplus.be.server.domain.order.OrderService
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderPointEventListener(private val orderService: OrderService) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: PointEvent.PaymentOrderUse) {
        orderService.complete(orderId = event.orderId, authentication = event.authentication)
    }
}