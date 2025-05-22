package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.event.OrderEvent
import kr.hhplus.be.server.domain.order.coupon.CouponVO
import kr.hhplus.be.server.domain.order.product.ProductVO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class OrderService(private val orderRepository: OrderRepository, private val orderEventPublisher: OrderEventPublisher) {
    fun findById(id: Long): Order = orderRepository.findById(id) ?: throw OrderException.OrderNotFound()
    fun findByForStatistics(query: OrderQuery.ForStatistics) = orderRepository.findByForStatistics(
        query
    )

    fun findForCancel(query: OrderQuery.ForCancelSchedule) = orderRepository.findForCancel(query)

    @Transactional
    fun create(command: OrderCommand.Create): Order {
        val (selectedProducts, selectedCoupons, authentication) = command
        val createOrder = CreateOrder(
            authentication = authentication
        )
        val order = orderRepository.create(createOrder)
        orderEventPublisher.createStart(
            OrderEvent.CreateStart(
                orderId = order.id,
                products = selectedProducts,
                publishedCouponIds = selectedCoupons,
                authentication = authentication,
            )
        )
        return order
    }

    @Transactional
    fun applyCoupons(orderId: Long, coupons: List<CouponVO>): Order {
        val order = findById(orderId)
        order.addCoupons(coupons)
        return order
    }

    @Transactional
    fun applyProducts(orderId: Long, products: List<ProductVO>): Order {
        val order = findById(orderId)
        order.addItems(products)
        return order
    }

    @Transactional
    fun applyPayment(orderId: Long, paymentId: Long): Order {
        val order = findById(orderId)
        order.applyPayment(paymentId)
        return order
    }

    @Transactional
    fun pay(orderId: Long, authentication: Authentication): Order {
        val order = findById(orderId)
        if (order.status != Order.Status.CREATED) throw IllegalStateException("정상적이지 않은 주문입니다.")
        orderEventPublisher.payStart(
            OrderEvent.PayStart(
                orderId = orderId, amount = order.totalPrice,
                authentication = authentication
            )
        )
        return order
    }

    @Transactional
    fun complete(orderId: Long, authentication: Authentication): Order {
        val order = findById(orderId)
        order.complete(authentication)
        return order
    }

    @Transactional
    fun cancel(command: OrderCommand.Cancel) {
        val (orderId, authentication) = command
        val order = findById(orderId)
        order.cancel(authentication)
        orderEventPublisher.cancel(
            event = OrderEvent.Cancel(
                orderId = order.id,
                products = order.receipt.items.map { Pair(it.productId, it.quantity) },
                publishedCouponIds = order.orderCoupons.items.map { it.id }
            )
        )
    }


    @Transactional
    fun createRankedProductByDate(to: LocalDate, day: Long) {
        val from = to.minusDays(day)
        val orders = orderRepository.findByForStatistics(OrderQuery.ForStatistics(Order.Status.COMPLETED, from, to))
        val rankedProducts =
            // List<List<OrderItem>>
            orders.map { it.receipt.items }
                // List<OrderItem>
                .flatten()
                // Map<Long(productId), List<OrderItem>>
                .groupBy { it.productId }
                // List<RankedProduct>
                .map { (productId, products) ->
                    OrderEvent.Ranked(
                        productId = productId,
                        totalSellingCount = products.sumOf { it.quantity },
                        totalIncome = products.sumOf { it.totalPrice },
                        createdDate = to
                    )
                }.forEach {
                    orderEventPublisher.ranked(
                        it
                    )
                }
    }
}