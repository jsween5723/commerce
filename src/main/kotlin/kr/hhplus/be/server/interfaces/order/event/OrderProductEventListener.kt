package kr.hhplus.be.server.interfaces.order.event

import kr.hhplus.be.server.domain.event.OrderEvent
import kr.hhplus.be.server.domain.event.OrderEventPublisher
import kr.hhplus.be.server.domain.event.ProductEvent
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.product.ProductVO
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderProductEventListener(
    private val orderService: OrderService,
    private val orderEventPublisher: OrderEventPublisher
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: ProductEvent.OrderDeduct) {
        val order = orderService.applyProducts(event.orderId, event.products.map {
            ProductVO(
                productId = it.productId,
                name = it.name,
                priceOfOne = it.priceOfOne,
                quantity = it.quantity,
            )
        })
        if (order.status == Order.Status.CREATED)
            orderEventPublisher.created(
                OrderEvent.Created(
                    orderId = order.id,
                    price = order.totalPrice,
                    authentication = event.authentication
                )
            )
    }
}