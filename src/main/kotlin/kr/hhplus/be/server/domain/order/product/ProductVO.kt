package kr.hhplus.be.server.domain.order.product

import jakarta.persistence.Column
import jakarta.persistence.Transient
import java.math.BigDecimal

data class ProductVO(
    @Column(
        name = "product_id", nullable = false
    ) val productId: Long,
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val priceOfOne: BigDecimal,
    @Column(nullable = false) val quantity: Long,
) {

    @get:Transient
    val totalPrice: BigDecimal get() = priceOfOne.multiply(BigDecimal(quantity))
}