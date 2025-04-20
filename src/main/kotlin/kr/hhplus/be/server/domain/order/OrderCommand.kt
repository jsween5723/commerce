package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.coupon.CouponVO
import kr.hhplus.be.server.domain.order.product.ProductVO

class OrderCommand {
    data class Create(
        val selectedProducts: List<ProductVO>,
        val publishedCoupons: List<CouponVO>,
        val authentication: Authentication
    )
}
