package kr.hhplus.be.server.domain.order.coupon

import jakarta.persistence.*
import kr.hhplus.be.server.domain.order.Order
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "used_coupons_to_order")
class UsedCouponToOrder private constructor(
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id", nullable = false) val order: Order,
    @Column(nullable = false) val couponId: Long,
    @Column(nullable = false) val publishedCouponId: Long,
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val description: String,
    @Column(nullable = false) val expireAt: LocalDateTime,
    @Enumerated(EnumType.STRING) @Column(nullable = false) val type: DiscountPolicy.Type,
    @Column(nullable = false) val amount: BigDecimal,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    @get:Transient
    private val discountPolicy: DiscountPolicy get() = DiscountPolicy.create(type, amount)

    fun discount(target: BigDecimal): BigDecimal = discountPolicy.discount(target)

    companion object {
        fun from(couponSnapshot: CouponSnapshot, order: Order): UsedCouponToOrder = UsedCouponToOrder(
            couponId = couponSnapshot.couponId,
            publishedCouponId = couponSnapshot.publishedCouponId,
            name = couponSnapshot.name,
            description = couponSnapshot.description,
            expireAt = couponSnapshot.expireAt,
            type = couponSnapshot.type,
            amount = couponSnapshot.amount,
            order = order,
        )

    }
}