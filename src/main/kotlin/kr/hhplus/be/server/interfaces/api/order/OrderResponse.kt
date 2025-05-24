package kr.hhplus.be.server.interfaces.api.order

import io.swagger.v3.oas.annotations.media.Schema

class OrderResponse {
    data class CreateOrderResponse(@Schema(description = "생성된 주문 id") val id: Long = 0)

    data class PayOrderResponse(@Schema(description = "완료된 주문 id") val id: Long = 0)
}