package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication

class CreateOrder(
    val authentication: Authentication,
) {
    fun toOrder(): Order {
        return Order(userId = authentication.id)
    }
}