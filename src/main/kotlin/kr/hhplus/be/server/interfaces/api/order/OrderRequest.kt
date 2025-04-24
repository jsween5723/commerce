package kr.hhplus.be.server.interfaces.api.order

import io.swagger.v3.oas.annotations.media.Schema
import kr.hhplus.be.server.application.order.OrderCriteria

class OrderRequest {
    data class CreateOrder(
        val orderItems: List<CreateOrderItem>, val registeredCouponIds: List<Long> = listOf(),
    ) {
        data class CreateOrderItem(val productId: Long, @Schema(description = "수량") val amount: Long) {
            fun toCriteria(): OrderCriteria.Create.CreateOrderItem {
                return OrderCriteria.Create.CreateOrderItem(productId = productId, amount = amount)
            }
        }
    }
}