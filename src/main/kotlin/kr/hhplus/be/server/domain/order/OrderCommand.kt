package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.coupon.SelectedCouponSnapshot
import kr.hhplus.be.server.domain.order.product.SelectedProductSnapshot

class OrderCommand {
    data class Create(
        val selectedProducts: List<SelectedProductSnapshot>,
        val publishedCoupons: List<SelectedCouponSnapshot>,
        val authentication: Authentication
    )
}
