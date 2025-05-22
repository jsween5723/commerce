package kr.hhplus.be.server.domain.order

import jakarta.persistence.*
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.order.coupon.CouponVO
import kr.hhplus.be.server.domain.order.coupon.OrderCoupon
import kr.hhplus.be.server.domain.order.coupon.UsedCoupons
import kr.hhplus.be.server.domain.order.product.OrderItem
import kr.hhplus.be.server.domain.order.product.ProductVO
import kr.hhplus.be.server.domain.order.product.Receipt
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "orders")
class Order(
    val userId: UserId,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
    val orderCoupons = UsedCoupons()
    val receipt: Receipt = Receipt()

    var paymentId: Long? = null

    @Enumerated(EnumType.STRING)
    var status: Status = Status.CREATE_PENDING

    @get:Transient
    val totalPrice: BigDecimal get() = orderCoupons.discount(receipt.totalPrice)

    private var couponProcess: Boolean? = null
    private var productProcess: Boolean? = null

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    fun addItems(items: List<ProductVO>) {
        receipt.addAll(items.map { OrderItem.from(it, this) })
        productProcess = true
        if (couponProcess == true) status = Status.CREATED
    }

    fun addCoupons(coupons: List<CouponVO>) {
        orderCoupons.addAll(coupons.map { OrderCoupon.from(it, this) })
        couponProcess = true
        if (productProcess == true) status = Status.CREATED
    }

    fun applyPayment(paymentId: Long) {
        this.paymentId = paymentId
    }

    fun complete(authentication: Authentication) {
        authentication.authorize(userId)
        if (status == Status.COMPLETED) throw OrderException.AleadyPaid()
        if (status != Status.CREATED) throw OrderException.PayOnlyPending()
        status = Status.COMPLETED
    }

    fun cancel(authentication: Authentication) {
        authentication.authorize(userId)
        if (status == Status.CANCELLED) throw OrderException.AleadyCancelled()
        if (status == Status.COMPLETED) throw OrderException.CancelOnlyNotReleased()
        status = Status.CANCELLED
    }

    enum class Status {
        CREATE_PENDING, CREATED, COMPLETED, CANCELLED
    }
}

