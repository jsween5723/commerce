package kr.hhplus.be.server.domain.order.product

import jakarta.persistence.*
import kr.hhplus.be.server.domain.order.Order
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity(name = "order_items")
class OrderItem private constructor(
    @JoinColumn(
        name = "order_id", nullable = false
    ) @ManyToOne(fetch = FetchType.LAZY) val order: Order,
// 프로덕트 도메인과 연관 끊기
    @Column(
        name = "product_id", nullable = false
    ) val productId: Long,
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
        fun from(orderedProductSnapshot: ProductSnapshot, order: Order) = OrderItem(
            order = order,
            productId = orderedProductSnapshot.id,
            name = orderedProductSnapshot.name,
            priceOfOne = orderedProductSnapshot.price,
            quantity = orderedProductSnapshot.quantity,
        )
    }
}