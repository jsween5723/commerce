package kr.hhplus.be.server.interfaces.product.event

import kr.hhplus.be.server.domain.event.OrderEvent
import kr.hhplus.be.server.domain.event.ProductEvent
import kr.hhplus.be.server.domain.event.ProductEventPublisher
import kr.hhplus.be.server.domain.product.ProductCommand
import kr.hhplus.be.server.domain.product.ProductService
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ProductOrderEventListener(
    private val productService: ProductService,
    private val eventPublisher: ProductEventPublisher
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: OrderEvent.CreateStart) {
        val released = productService.release(ProductCommand.Release(event.products.map { (productId, quantity) ->
            ProductCommand.ProductIdAndQuantity(
                productId,
                quantity
            )
        }))
        eventPublisher.orderDeduct(ProductEvent.OrderDeduct(orderId = event.orderId, products = released.map {
            ProductEvent.OrderItem(
                productId = it.product.id,
                name = it.product.name,
                priceOfOne = it.product.price,
                quantity = it.quantity
            )
        }, authentication = event.authentication))
    }
}