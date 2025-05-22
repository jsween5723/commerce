package kr.hhplus.be.server.domain.order.product

import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany
import jakarta.persistence.Transient
import java.math.BigDecimal
import java.util.*

@Embeddable
class Receipt(
    @OneToMany(
        mappedBy = "order", cascade = [(CascadeType.ALL)], orphanRemoval = true
    ) val items: MutableList<OrderItem> = LinkedList()
) {
    @get: Transient
    val totalPrice: BigDecimal get() = items.sumOf { it.totalPrice }

    fun addAll(items: List<OrderItem>) {
        this.items.addAll(items)
    }

    fun add(item: OrderItem) {
        this.items.add(item)
    }
}