package kr.hhplus.be.server.application.order

import io.swagger.v3.oas.annotations.media.Schema
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.product.ProductCommand
import java.time.LocalDateTime

class OrderCriteria {
    data class Create(
        val orderItems: List<CreateOrderItem>,
        val registeredCouponIds: List<Long>,
        val authentication: Authentication
    ) {
        data class CreateOrderItem(
            val productId: Long, @Schema(description = "수량") val amount: Long
        ) {
            fun toProductIdAndQuantity() =
                ProductCommand.ProductIdAndQuantity(productId, amount)
        }

        fun toProductReleaseCommand() =
            ProductCommand.Release(orderItems.map { it.toProductIdAndQuantity() })
    }

    data class Pay(val orderId: Long, val authentication: Authentication)
    data class Cancel(val orderId: Long, val authentication: Authentication)
    data class CancelBy(val pendingTime: LocalDateTime, val authentication: Authentication)
}