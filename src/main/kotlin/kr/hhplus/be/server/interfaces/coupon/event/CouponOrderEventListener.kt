package kr.hhplus.be.server.interfaces.coupon.event

import kr.hhplus.be.server.domain.coupon.CouponCommand
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.event.CouponEvent
import kr.hhplus.be.server.domain.coupon.CouponEventPublisher
import kr.hhplus.be.server.domain.event.OrderEvent
import kr.hhplus.be.server.domain.order.coupon.DiscountPolicy
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

class CouponOrderEventListener(
    private val couponService: CouponService,
    private val couponEventPublisher: CouponEventPublisher
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    fun handle(event: OrderEvent.CreateStart) {
        val coupons = couponService.selectPublishedCoupons(
            CouponCommand.Select(
                authentication = event.authentication,
                publishedCouponIds = event.publishedCouponIds
            )
        )
        couponEventPublisher.used(
            CouponEvent.OrderUsed(
                event.orderId,
                coupons.map {
                    CouponEvent.OrderCoupon(
                        userId = it.userId.userId,
                        couponId = it.coupon.id,
                        publishedCouponId = it.id,
                        name = it.name,
                        description = it.description,
                        expireAt = it.expireAt,
                        type = DiscountPolicy.Type.fromString(it.discountType.name),
                        amount = it.discountAmount
                    )
                },
                authentication = event.authentication,
            )
        )
    }
}