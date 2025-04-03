package kr.hhplus.be.server.api.coupon

import kr.hhplus.be.server.api.Authentication
import kr.hhplus.be.server.api.Response
import kr.hhplus.be.server.api.SuccessResponse
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/coupons")
class CouponController : CouponSpec {
    override fun register(authentication: Authentication): Response<RegisterCouponResponse> {
        return SuccessResponse(RegisterCouponResponse(id = 1))
    }

    override fun getMyRegisteredCoupons(authentication: Authentication): Response<GetMyRegisteredCouponsResponse> {
        return SuccessResponse(
            GetMyRegisteredCouponsResponse(
                coupons = listOf(
                    GetMyRegisteredCouponsResponse.RegisteredCoupon(
                        id = 3721,
                        userId = 1636,
                        coupon = GetMyRegisteredCouponsResponse.Coupon(
                            id = 4092,
                            name = "비율 할인 쿠폰",
                            description = "모든 상품에 비율할인을 적용합니다.",
                            type = GetMyRegisteredCouponsResponse.DiscountType.PERCENT,
                            amount = 2.3
                        ),
                        expiredAt = LocalDateTime.now()
                            .plusDays(3)
                    )
                )
            )
        )
    }
}