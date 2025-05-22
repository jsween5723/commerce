package kr.hhplus.be.server.interfaces.event.order

import kr.hhplus.be.server.domain.event.CouponEvent
import kr.hhplus.be.server.domain.event.OrderEvent
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderEventPublisher
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.coupon.CouponVO
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderCouponEventListener(
    private val orderService: OrderService,
    private val orderEventPublisher: OrderEventPublisher
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: CouponEvent.OrderUsed) {
        val order = orderService.applyCoupons(event.orderId, event.coupons.map {
            CouponVO(
                couponId = it.couponId,
                publishedCouponId = it.publishedCouponId,
                name = it.name,
                description = it.description,
                expireAt = it.expireAt,
                type = it.type,
                amount = it.amount
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