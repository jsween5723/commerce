package kr.hhplus.be.server.domain.event

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.coupon.DiscountPolicy
import java.math.BigDecimal
import java.time.LocalDateTime

class CouponEvent {
    data class OrderUsed(
        val orderId: Long, val coupons: List<OrderCoupon>, val authentication: Authentication
    )

    data class OrderCoupon(
        val userId: Long, val couponId: Long,
        val publishedCouponId: Long,
        val name: String,
        val description: String,
        val expireAt: LocalDateTime,
        val type: DiscountPolicy.Type,
        val amount: BigDecimal,
    )
}
