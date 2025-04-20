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
    @Enumerated(EnumType.STRING) @Column(nullable = false) val discountType: DiscountPolicy.Type,
//    할인수치
    @Column(nullable = false) val discountAmount: BigDecimal,
//    수량
    @Column(nullable = false) var stock: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    fun publish(userId: UserId, now: LocalDateTime): PublishedCoupon {
        if (publishFrom.isAfter(now) || publishTo.isBefore(now)) throw CouponException.CouponNotPublishing()
        if (stock < 1) throw CouponException.CouponStockUnavailable()
        val publishedCoupon = PublishedCoupon.from(userId, this, now)
        stock = stock.minus(1)
        return publishedCoupon
    }
}