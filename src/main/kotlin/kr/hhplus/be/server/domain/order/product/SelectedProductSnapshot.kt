package kr.hhplus.be.server.domain.order.product

import java.math.BigDecimal

data class SelectedProductSnapshot(val id: Long, val name: String, val quantity: Long, val price: BigDecimal) {
    val totalPrice: BigDecimal = price.multiply(BigDecimal(quantity))
}