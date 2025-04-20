package kr.hhplus.be.server.domain.order.coupon

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Embeddable
data class CouponVO(
    @Column(nullable = false) val couponId: Long,
    @Column(nullable = false) val publishedCouponId: Long,
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val description: String,
    @Column(nullable = false) val expireAt: LocalDateTime,
    @Enumerated(EnumType.STRING) @Column(nullable = false) val type: DiscountPolicy.Type,
    @Column(nullable = false) val amount: BigDecimal,
) {
    @get:Transient
    private val discountPolicy: DiscountPolicy get() = DiscountPolicy.create(type, amount)

    fun discount(target: BigDecimal): BigDecimal = discountPolicy.discount(target)
}