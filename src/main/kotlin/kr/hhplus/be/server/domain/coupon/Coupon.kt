package kr.hhplus.be.server.domain.coupon

import jakarta.persistence.*
import kr.hhplus.be.server.domain.auth.UserId
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.Duration
import java.time.LocalDateTime

// 레포지터리에서 가져오는 요소이므로 별도로 검증하지 않는다.
// 추후 통합테스트를 할 때 Create클래스에서 검증예정
@Entity(name = "coupons")
class Coupon(
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val description: String,
//    배포시각
    @Column(nullable = false) val publishFrom: LocalDateTime,
    @Column(nullable = false) val publishTo: LocalDateTime,
//   발급 후 ~ 만료 까지 기간
    @Column(nullable = false) val expireDuration: Duration,
    @Enumerated(EnumType.STRING) @Column(nullable = false) val type: Type,
//    수치
    @Column(nullable = false) val amount: BigDecimal,
//    수량
    @Column(nullable = false) var stock: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    @OneToMany(mappedBy = "coupon", cascade = [CascadeType.MERGE, CascadeType.REMOVE], orphanRemoval = true)
    val publishedCoupons: MutableList<PublishedCoupon> = mutableListOf()

    enum class Type {
        PERCENT, FIXED
    }

    @get:Transient
    private val discountPolicy: DiscountPolicy get() = DiscountPolicy.create(type, amount)

    fun publish(userId: UserId, publishedAt: LocalDateTime): PublishedCoupon {
        if (publishFrom.isAfter(publishedAt) || publishTo.isBefore(publishedAt)) throw CouponException.CouponNotPublishing()
        if (stock < 1) throw CouponException.CouponStockUnavailable()
        val publishedCoupon = PublishedCoupon(
            userId = userId, expireAt = publishedAt.plus(expireDuration), coupon = this
        )
        this.publishedCoupons.add(
            publishedCoupon
        )
        stock = stock.minus(1)
        return publishedCoupon
    }

    fun discount(target: BigDecimal): BigDecimal =
        discountPolicy.discount(target)

}