package kr.hhplus.be.server.domain.event

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.product.ProductException
import java.math.BigDecimal
import java.time.LocalDate

class OrderEvent {
    data class Ranked(
        val productId: Long,
        val totalSellingCount: Long,
        val totalIncome: BigDecimal,
        val createdDate: LocalDate
    )

    data class CreateStart(
        val orderId: Long,
        val products: List<Pair<Long, Long>>,
        val publishedCouponIds: List<Long>,
        val authentication: Authentication
    ) {
        init {
            if (products.isEmpty()) throw ProductException.TargetIsEmpty()
            products.forEach { (productId, quantity) ->
                if (productId < 1) throw ProductException.InvalidProductId()
                if (quantity < 1) throw ProductException.AmountMustGreaterThanZero()
            }
            publishedCouponIds.forEach { if (it < 1) throw IllegalArgumentException("$it 는 유효하지 않은 쿠폰 id 입니다.") }
        }
    }

    data class Created(
        val orderId: Long,
        val price: BigDecimal,
        val authentication: Authentication
    )

    data class CancelStart(
        val orderId: Long,
        val products: List<Pair<Long, Long>>,
        val publishedCouponIds: List<Long>
    )

    data class Complete(
        val order: Order
    )

    data class PayStart(val orderId: Long, val amount: BigDecimal, val authentication: Authentication)
}