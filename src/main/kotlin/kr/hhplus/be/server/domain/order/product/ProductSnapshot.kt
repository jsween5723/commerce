package kr.hhplus.be.server.domain.order.product

import java.math.BigDecimal

data class ProductSnapshot(
    val id: Long,
    val name: String,
    val quantity: Long,
    val price: BigDecimal
) {
    val totalPrice: BigDecimal get() = price.multiply(BigDecimal(quantity))
}