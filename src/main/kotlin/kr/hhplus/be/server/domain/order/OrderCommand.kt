package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication

class OrderCommand {
    data class Create(
        val orderItems: List<Pair<Long, Long>>, val publishedCouponIds: List<Long>, val authentication: Authentication
    )

    data class Cancel(val orderId: Long, val authentication: Authentication)
}