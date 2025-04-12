package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.coupon.PublishedCoupon
import kr.hhplus.be.server.domain.product.Product

class OrderCommand {
    data class Create(
        val releaseItems: List<Product.ReleaseInfo>,
        val publishedCoupons: List<PublishedCoupon>,
        val authentication: Authentication
    )
}
