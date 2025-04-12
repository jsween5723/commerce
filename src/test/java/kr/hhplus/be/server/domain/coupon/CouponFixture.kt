package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.order.coupon.SelectedCouponSnapshot
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime

class CouponFixture(
    name: String = "Test Coupon",
    description: String = "Test Description",
//    배포시각
    publishFrom: LocalDateTime = LocalDateTime.now().minusDays(2),
    publishTo: LocalDateTime = LocalDateTime.now().plusDays(1),
//   발급 후 ~ 만료 까지 기간
    expireDuration: Duration = Duration.ofDays(3),
    type: DiscountPolicy.Type = DiscountPolicy.Type.FIXED,
//    수치
    amount: BigDecimal = BigDecimal.valueOf(20),
//    수량
    stock: Long = 20L
) : Coupon(
    name = name,
    description = description,
    publishFrom = publishFrom,
    publishTo = publishTo,
    expireDuration = expireDuration,
    type = type,
    amount = amount,
    stock = stock
)

class PublishedCouponFixture(
    userId: UserId = UserId(1L),
    expireAt: LocalDateTime = LocalDateTime.now().plusDays(2),
    usedAt: LocalDateTime? = null,
    coupon: Coupon = CouponFixture()
) : PublishedCoupon(
    userId = userId, expireAt = expireAt, usedAt = usedAt, coupon = coupon
)

fun CouponSnapshotFixture() = SelectedCouponSnapshot(
    couponId = 8971,
    publishedCouponId = 9992,
    name = "Pamela Shields",
    description = "curabitur",
    expireAt = LocalDateTime.now().plusDays(2),
    type = kr.hhplus.be.server.domain.order.coupon.DiscountPolicy.Type.PERCENT,
    amount = BigDecimal.valueOf(20),
)