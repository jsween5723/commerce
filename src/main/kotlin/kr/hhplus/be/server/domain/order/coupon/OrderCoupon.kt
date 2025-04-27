package kr.hhplus.be.server.domain.order.coupon

import jakarta.persistence.*
import kr.hhplus.be.server.domain.order.Order
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "order_coupons")
class OrderCoupon private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,
    val coupon: CouponVO
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    fun discount(target: BigDecimal): BigDecimal = coupon.discount(target)

    companion object {
        fun from(couponVO: CouponVO, order: Order): OrderCoupon = OrderCoupon(
            coupon = couponVO,
            order = order,
        )
    }
}