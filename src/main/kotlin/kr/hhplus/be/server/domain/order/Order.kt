package kr.hhplus.be.server.domain.order

import jakarta.persistence.*
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.order.coupon.CouponSnapshot
import kr.hhplus.be.server.domain.order.coupon.UsedCouponToOrder
import kr.hhplus.be.server.domain.order.coupon.UsedCoupons
import kr.hhplus.be.server.domain.order.payment.Payment
import kr.hhplus.be.server.domain.order.product.OrderItem
import kr.hhplus.be.server.domain.order.product.ProductSnapshot
import kr.hhplus.be.server.domain.order.product.Receipt
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@Entity(name = "orders")
class Order protected constructor(
    productSnapshots: List<ProductSnapshot>,
    val userId: UserId,
    selectedCoupons: List<CouponSnapshot>
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L
    val usedCoupons = UsedCoupons(selectedCoupons.map { UsedCouponToOrder.from(it, this) })
    val receipt: Receipt = Receipt(productSnapshots.map { OrderItem.from(it, this) })

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

    class Create(
        private val productSnapshots: List<ProductSnapshot>,
        private val authentication: Authentication,
        private val couponSnapshots: List<CouponSnapshot> = listOf(),
    ) {
        init {
            validateProducts(productSnapshots)
            validateCoupons(couponSnapshots)
        }

        private fun validateProducts(productSnapshots: List<ProductSnapshot>) {
            if (productSnapshots.isEmpty()) throw OrderException.ReciptIsEmpty()
            productSnapshots.forEach { if (it.quantity < 1L) throw OrderException.OrderItemIsGreaterThanZero() }
        }

        private fun validateCoupons(couponSnapshots: List<CouponSnapshot>) {
            couponSnapshots.forEach {
                if (now().isAfter(it.expireAt)) throw IllegalStateException("${it.expireAt}에 만료된 쿠폰입니다.")
            }
        }

        fun toOrder(): Order {
            return Order(
                productSnapshots = productSnapshots,
                userId = authentication.id,
                selectedCoupons = couponSnapshots
            )
        }
    }
}

