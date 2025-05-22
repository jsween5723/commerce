package kr.hhplus.be.server.interfaces.order.api

import io.swagger.v3.oas.annotations.media.Schema
import kr.hhplus.be.server.domain.order.OrderCommand

class OrderRequest {
    data class CreateOrder(
        val orderItems: List<CreateOrderItem>, val registeredCouponIds: List<Long> = listOf(),
    ) {
        data class CreateOrderItem(val productId: Long, @Schema(description = "수량") val amount: Long) {
            fun toCriteria(): OrderCommand.Create.CreateOrderItem {
                return OrderCommand.Create.CreateOrderItem(productId = productId, amount = amount)
            }
        }
    }
}