package kr.hhplus.be.server.domain.order.coupon

import java.math.BigDecimal
import java.time.LocalDateTime

fun CouponSnapshotFixture() = CouponVO(
    couponId = 8971,
    publishedCouponId = 9992,
    name = "Pamela Shields",
    description = "curabitur",
    expireAt = LocalDateTime.now().plusDays(2),
    type = kr.hhplus.be.server.domain.order.coupon.DiscountPolicy.Type.PERCENT,
    amount = BigDecimal.valueOf(20),
)