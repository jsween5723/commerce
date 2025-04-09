package kr.hhplus.be.server.domain.order

import jakarta.persistence.*
import kr.hhplus.be.server.domain.coupon.PublishedCoupon
import java.math.BigDecimal

@Entity(name = "used_coupons_to_order")
class UsedCouponToOrder(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "published_coupon_id", nullable = false)
    val publishedCoupon: PublishedCoupon
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    lateinit var order: Order

    fun deuse() {
        publishedCoupon.deuse()
    }

    fun discount(target: BigDecimal): BigDecimal = publishedCoupon.discount(target)
}