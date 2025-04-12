package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.coupon.PublishedCoupon
import kr.hhplus.be.server.domain.order.coupon.DiscountPolicy
import kr.hhplus.be.server.domain.order.coupon.SelectedCouponSnapshot
import kr.hhplus.be.server.domain.order.product.SelectedProductSnapshot
import kr.hhplus.be.server.domain.product.Product
import org.springframework.stereotype.Component

@Component
class OrderMapper {
    fun toSelectedProductAndQuantitySnapshot(releaseInfo: Product.ReleaseInfo): SelectedProductSnapshot {
        return SelectedProductSnapshot(
            id = releaseInfo.product.id,
            name = releaseInfo.product.name,
            quantity = releaseInfo.quantity,
            price = releaseInfo.product.price
        )
    }

    fun toSelectedCouponSnapshot(publishedCoupon: PublishedCoupon): SelectedCouponSnapshot {
        return SelectedCouponSnapshot(
            couponId = publishedCoupon.coupon.id,
            publishedCouponId = publishedCoupon.id,
            name = publishedCoupon.coupon.name,
            description = publishedCoupon.coupon.description,
            expireAt = publishedCoupon.expireAt,
            type = DiscountPolicy.Type.fromString(publishedCoupon.coupon.type.name),
            amount = publishedCoupon.coupon.amount,
        )
    }
}