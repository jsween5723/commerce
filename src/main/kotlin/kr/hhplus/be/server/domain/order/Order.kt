package kr.hhplus.be.server.domain.order

import jakarta.persistence.*
import kr.hhplus.be.server.domain.auth.AuthException
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.payment.Payment
import kr.hhplus.be.server.domain.product.Product
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity(name = "orders")
class Order private constructor(
    private val receipt: Receipt, @Column(nullable = false) val userId: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    //    주문 생성시 결제는 같은 시점에 대기 상태로 생성돼야하므로
//    cascade persist를 지정한다.
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "payment_id", nullable = false)
    var payment: Payment = Payment.from(this)

    @Enumerated(EnumType.STRING)
    var status: Status = Status.PENDING

    @get:Transient
    val totalPrice: BigDecimal get() = receipt.totalPrice

    @get:Transient
    val paymentStatus: Payment.Status get() = payment.status

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    fun pay(authentication: Authentication): OrderInfo.Pay {
        authorize(authentication)
        if (status == Status.PAID) throw OrderException.AleadyPaid()
        if (status != Status.PENDING) throw OrderException.PayOnlyPending()
        val payInfo = payment.pay(authentication)
        status = Status.PAID
        return OrderInfo.Pay(payInfo.pointAmount)
    }

    fun cancel(authentication: Authentication): OrderInfo.Cancel {
        authorize(authentication)
        if (status == Status.CANCELLED) throw OrderException.AleadyCancelled()
        if (status == Status.RELEASED) throw OrderException.CancelOnlyNotReleased()
        val cancelInfo = payment.cancel(authentication)
//        TODO: receipt.restock()
        status = Status.CANCELLED
        return OrderInfo.Cancel(cancelInfo.pointAmount)
    }

    private fun authorize(authentication: Authentication) {
        if (authentication.userId != userId) throw AuthException.ForbiddenException()
    }

    companion object {
        fun from(createOrder: CreateOrder) =
            Order(receipt = Receipt.from(createOrder.receipt), userId = createOrder.userId)
    }

    enum class Status {
        PENDING, PAID, RELEASED, CANCELLED
    }
}

@Embeddable
class Receipt protected constructor(
    @OneToMany(
        cascade = [(CascadeType.ALL)], orphanRemoval = true
    ) val items: MutableList<OrderItem> = LinkedList()
) {
    @get: Transient
    val totalPrice: BigDecimal get() = items.sumOf { it.totalPrice }

    companion object {
        fun from(createReceipt: CreateReceipt) = Receipt(createReceipt.items.map { OrderItem.from(it) }.toMutableList())
    }
}


@Entity(name = "order_items")
class OrderItem protected constructor(
    @JoinColumn(
        name = "order_id", nullable = false
    ) @ManyToOne(fetch = FetchType.LAZY) val order: Order?,
    @JoinColumn(
        name = "product_id", nullable = false
    ) @ManyToOne(fetch = FetchType.LAZY) val product: Product,
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val priceOfOne: BigDecimal,
    @Column(nullable = false) val quantity: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    @get:Transient
    val totalPrice: BigDecimal get() = priceOfOne.multiply(BigDecimal(quantity))

    companion object {
        fun from(createOrderItem: CreateOrderItem) = OrderItem(
            order = null,
            product = createOrderItem.product,
            name = createOrderItem.name,
            priceOfOne = createOrderItem.priceOfOne,
            quantity = createOrderItem.quantity
        )
    }
}