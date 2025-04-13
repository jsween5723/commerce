package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.coupon.CouponSnapshot
import kr.hhplus.be.server.domain.order.product.ProductSnapshot

class OrderCommand {
    data class Create(
        val selectedProducts: List<ProductSnapshot>,
        val publishedCoupons: List<CouponSnapshot>,
        val authentication: Authentication
    )
}
