package kr.hhplus.be.server.interfaces.order.api

import io.swagger.v3.oas.annotations.media.Schema

class OrderRequest {
    data class CreateOrder(
        val orderItems: List<CreateOrderItem>, val registeredCouponIds: List<Long> = listOf(),
    ) {
        data class CreateOrderItem(val productId: Long, @Schema(description = "수량") val amount: Long)
    }
}