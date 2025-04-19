package kr.hhplus.be.server.interfaces.api.coupon

import io.swagger.v3.oas.annotations.media.Schema
import kr.hhplus.be.server.domain.coupon.DiscountPolicy
import kr.hhplus.be.server.domain.coupon.PublishedCoupon
import java.math.BigDecimal
import java.time.LocalDateTime

class CouponResponse {
    data class RegisterCouponResponse(@Schema(description = "사용자에게 등록된 쿠폰의 id") val id: Long)
    data class GetMyRegisteredCouponsResponse(val coupons: List<RegisteredCoupon>) {
        data class RegisteredCoupon(
            @Schema(description = "등록된 쿠폰 id") val id: Long,
            val userId: Long,
            val coupon: Coupon,
            val expiredAt: LocalDateTime,
            val usedAt: LocalDateTime?,
            val createdAt: LocalDateTime,
        )

        data class Coupon(
            val id: Long,
            val name: String,
            val description: String,
            val type: DiscountPolicy.Type,
            val amount: BigDecimal,
        )

        enum class DiscountType {
            PERCENT, FIXED
        }

        companion object {
            fun from(publishedCoupons: List<PublishedCoupon>): GetMyRegisteredCouponsResponse {
                return GetMyRegisteredCouponsResponse(publishedCoupons.map {
                    RegisteredCoupon(
                        id = it.id, userId = it.userId.userId, coupon = Coupon(
                            id = it.coupon.id,
                            name = it.coupon.name,
                            description = it.coupon.description,
                            type = it.coupon.discountType,
                            amount = it.coupon.discountAmount
                        ), expiredAt = it.expireAt, usedAt = it.usedAt, createdAt = it.createdAt
                    )
                })
            }
        }
    }
}