package kr.hhplus.be.server.interfaces.coupon

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.interfaces.Response
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/coupons")
class CouponController : CouponSpec {
    @PostMapping("{id}/register")
    override fun register(
        authentication: Authentication,
        @PathVariable id: Long
    ): Response<RegisterCouponResponse> {
        return Response.success(RegisterCouponResponse(id = 1))
    }

    @GetMapping("/me")
    override fun getMyRegisteredCoupons(authentication: Authentication): Response<GetMyRegisteredCouponsResponse> {
        return Response.success(
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