package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.coupon.CouponVO
import kr.hhplus.be.server.domain.order.product.ProductVO
import java.time.LocalDateTime.now

class CreateOrder(
    val productVOS: List<ProductVO>,
    val authentication: Authentication,
    val couponVOS: List<CouponVO> = listOf(),
) {
    init {
        validateProducts(productVOS)
        validateCoupons(couponVOS)
    }

    private fun validateProducts(productVOS: List<ProductVO>) {
        if (productVOS.isEmpty()) throw OrderException.ReciptIsEmpty()
        productVOS.forEach { if (it.quantity < 1L) throw OrderException.OrderItemIsGreaterThanZero() }
    }

    private fun validateCoupons(couponVOS: List<CouponVO>) {
        couponVOS.forEach {
            if (now().isAfter(it.expireAt)) throw OrderException.ExpiredCoupons()
        }
    }

    fun toOrder(): Order {
        return Order(productVOS = productVOS, userId = authentication.id, selectedCoupons = couponVOS)
    }
}