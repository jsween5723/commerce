package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.auth.UserId
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
    discountType = type,
    discountAmount = amount,
    stock = stock
)

fun PublishedCouponFixture(
    userId: UserId = UserId(1L),
    now: LocalDateTime = LocalDateTime.now(),
    coupon: Coupon = CouponFixture()
) = PublishedCoupon.from(
    userId = userId, now = now, coupon = coupon
)