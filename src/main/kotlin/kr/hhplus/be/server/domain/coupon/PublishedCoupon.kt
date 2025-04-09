package kr.hhplus.be.server.domain.coupon

import jakarta.persistence.*
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "published_coupon")
class PublishedCoupon(
    val userId: UserId,
    val expireAt: LocalDateTime,
    var usedAt: LocalDateTime? = null,
    coupon: Coupon? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    @JoinColumn(name = "coupon_id")
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var coupon: Coupon

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    init {
        if (coupon != null) {
            this.coupon = coupon
        }
    }

    @get:Transient
    val used: Boolean get() = usedAt != null

    fun use(time: LocalDateTime, authentication: Authentication) {
        if (used) throw CouponException.CouponAlreadyUsed()
        if (time > expireAt) throw CouponException.CouponExpired()
        usedAt = time
    }

    fun deuse() {
        usedAt = null
    }

    fun discount(target: BigDecimal): BigDecimal = coupon.discount(target)
}

//PublishedCoupon(
//    userId = userId, expireAt = expireAt, usedAt = usedAt, orderId = orderId, coupon = CouponFixture()
//)