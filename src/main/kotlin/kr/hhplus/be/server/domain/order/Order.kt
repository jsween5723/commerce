package kr.hhplus.be.server.domain.order

import jakarta.persistence.*
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.order.coupon.CouponVO
import kr.hhplus.be.server.domain.order.coupon.UsedCoupon
import kr.hhplus.be.server.domain.order.coupon.UsedCoupons
import kr.hhplus.be.server.domain.order.payment.Payment
import kr.hhplus.be.server.domain.order.product.OrderItem
import kr.hhplus.be.server.domain.order.product.ProductVO
import kr.hhplus.be.server.domain.order.product.Receipt
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "orders")
class Order(
    productVOS: List<ProductVO>,
    val userId: UserId,
    selectedCoupons: List<CouponVO>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    val usedCoupons = UsedCoupons(selectedCoupons.map { UsedCoupon.from(it, this) })
    val receipt: Receipt = Receipt(productVOS.map { OrderItem.from(it, this) })

    //    주문 생성시 결제는 같은 시점에 대기 상태로 생성돼야하므로
//    cascade persist를 지정한다.
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "payment_id")
    var payment: Payment = Payment.from(this)

    @Enumerated(EnumType.STRING)
    var status: Status = Status.PENDING

    @get:Transient
    val totalPrice: BigDecimal get() = usedCoupons.discount(receipt.totalPrice)

    @get:Transient
    val paymentStatus: Payment.Status get() = payment.status

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    fun pay(authentication: Authentication): OrderInfo.Pay {
        authentication.authorize(userId)
        if (status == Status.PAID) throw OrderException.AleadyPaid()
        if (status != Status.PENDING) throw OrderException.PayOnlyPending()
        val payInfo = payment.pay(authentication)
        status = Status.PAID
        return OrderInfo.Pay(payInfo.pointAmount)
    }

    fun cancel(authentication: Authentication): OrderInfo.Cancel {
        authentication.authorize(userId)
        if (status == Status.CANCELLED) throw OrderException.AleadyCancelled()
        if (status == Status.RELEASED) throw OrderException.CancelOnlyNotReleased()
        val cancelInfo = payment.cancel(authentication)
        status = Status.CANCELLED
        return OrderInfo.Cancel(cancelInfo.pointAmount)
    }

    enum class Status {
        PENDING, PAID, RELEASED, CANCELLED
    }
}

