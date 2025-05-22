package kr.hhplus.be.server.domain.event

import kr.hhplus.be.server.domain.auth.Authentication
import java.math.BigDecimal

class ProductEvent {
    data class OrderDeduct(val orderId: Long, val products: List<OrderItem>, val authentication: Authentication)
    data class OrderItem(
        val productId: Long,
        val name: String,
        val priceOfOne: BigDecimal,
        val quantity: Long,
    )
}