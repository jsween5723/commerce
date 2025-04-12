package kr.hhplus.be.server.domain.order

import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany
import jakarta.persistence.Transient
import java.math.BigDecimal
import java.util.*

@Embeddable
class Receipt protected constructor(
    @OneToMany(
        mappedBy = "order", cascade = [(CascadeType.ALL)], orphanRemoval = true
    ) val items: MutableList<OrderItem> = LinkedList()
) {
    @get: Transient
    val totalPrice: BigDecimal get() = items.sumOf { it.totalPrice }

    companion object {
        fun from(createReceipt: CreateReceipt, order: Order) =
            Receipt(createReceipt.items.map { OrderItem.from(it, order) }.toMutableList())
    }
}