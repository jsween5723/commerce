package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.coupon.CouponSnapshot
import kr.hhplus.be.server.domain.order.product.ProductSnapshot
import java.time.LocalDateTime.now

class CreateOrder(
    val productSnapshots: List<ProductSnapshot>,
    val authentication: Authentication,
    val couponSnapshots: List<CouponSnapshot> = listOf(),
) {
    init {
        validateProducts(productSnapshots)
        validateCoupons(couponSnapshots)
    }

    private fun validateProducts(productSnapshots: List<ProductSnapshot>) {
        if (productSnapshots.isEmpty()) throw OrderException.ReciptIsEmpty()
        productSnapshots.forEach { if (it.quantity < 1L) throw OrderException.OrderItemIsGreaterThanZero() }
    }

    private fun validateCoupons(couponSnapshots: List<CouponSnapshot>) {
        couponSnapshots.forEach {
            if (now().isAfter(it.expireAt)) throw IllegalStateException("${it.expireAt}에 만료된 쿠폰입니다.")
        }
    }

    fun toOrder(): Order {
        return Order(productSnapshots = productSnapshots, userId = authentication.id, selectedCoupons = couponSnapshots)
    }
}