package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.coupon.PublishedCoupon
import kr.hhplus.be.server.domain.order.coupon.CouponSnapshot
import kr.hhplus.be.server.domain.order.coupon.DiscountPolicy
import kr.hhplus.be.server.domain.order.product.ProductSnapshot
import kr.hhplus.be.server.domain.order.product.Receipt
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductCommand

object OrderMapper {
    fun toSelectedProductAndQuantitySnapshot(releaseInfo: Product.ReleaseInfo): ProductSnapshot {
        return ProductSnapshot(
            id = releaseInfo.product.id,
            name = releaseInfo.product.name,
            quantity = releaseInfo.quantity,
            price = releaseInfo.product.price
        )
    }

    fun toSelectedCouponSnapshot(publishedCoupon: PublishedCoupon): CouponSnapshot {
        return CouponSnapshot(
            couponId = publishedCoupon.coupon.id,
            publishedCouponId = publishedCoupon.id,
            name = publishedCoupon.coupon.name,
            description = publishedCoupon.coupon.description,
            expireAt = publishedCoupon.expireAt,
            type = DiscountPolicy.Type.fromString(publishedCoupon.coupon.discountType.name),
            amount = publishedCoupon.coupon.discountAmount,
        )
    }

    fun toProductIdAndQuantities(receipt: Receipt): List<ProductCommand.ProductIdAndQuantity> {
        return receipt.items.map { orderItem ->
            ProductCommand.ProductIdAndQuantity(
                orderItem.productId, orderItem.quantity
            )
        }
    }
}