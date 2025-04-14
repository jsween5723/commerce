package kr.hhplus.be.server.domain.coupon

import jakarta.persistence.*
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "published_coupons")
class PublishedCoupon protected constructor(
    val userId: UserId,
    @Column(nullable = false) val expireAt: LocalDateTime,
    var usedAt: LocalDateTime? = null,
    @JoinColumn(name = "coupon_id") @ManyToOne(fetch = FetchType.LAZY) val coupon: Coupon,
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val description: String,
    @Enumerated(EnumType.STRING) @Column(nullable = false) val discountType: DiscountPolicy.Type,
    @Column(nullable = false) val discountAmount: BigDecimal,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    @get:Transient
    val used: Boolean get() = usedAt != null

    fun use(time: LocalDateTime, authentication: Authentication) {
        authentication.authorize(userId)
        if (used) throw CouponException.CouponAlreadyUsed()
        if (time > expireAt) throw CouponException.CouponExpired()
        usedAt = time
    }

    fun deuse() {
        usedAt = null
    }

    companion object {
        fun from(userId: UserId, coupon: Coupon, now: LocalDateTime): PublishedCoupon = PublishedCoupon(
            userId = userId, expireAt = now.plus(coupon.expireDuration), coupon = coupon,
            usedAt = null,
            name = coupon.name,
            description = coupon.description,
            discountType = coupon.discountType,
            discountAmount = coupon.discountAmount
        )

    }
}

//PublishedCoupon(
//    userId = userId, expireAt = expireAt, usedAt = usedAt, orderId = orderId, coupon = CouponFixture()
//)