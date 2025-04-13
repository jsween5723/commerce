package kr.hhplus.be.server.domain.order.coupon

import java.math.BigDecimal
import java.time.LocalDateTime

data class CouponSnapshot(
    val couponId: Long,
    val publishedCouponId: Long,
    val name: String,
    val description: String,
    val expireAt: LocalDateTime,
    val type: DiscountPolicy.Type,
    val amount: BigDecimal,
)