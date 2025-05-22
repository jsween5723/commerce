package kr.hhplus.be.server.interfaces.api.coupon

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.coupon.CouponCommand
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.interfaces.api.Response
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/coupons")
class CouponController(private val couponService: CouponService) : CouponSpec {
    @PostMapping("{id}/register")
    override fun register(
        authentication: Authentication,
        @PathVariable id: Long
    ): Response<CouponResponse.RegisterCouponResponse> {
        val published = couponService.publish(
            CouponCommand.Publish(
                authentication = authentication,
                couponId = id
            )
        )
        return Response.success(CouponResponse.RegisterCouponResponse(id = published.id))
    }

    @GetMapping("/me")
    override fun getMyRegisteredCoupons(authentication: Authentication): Response<CouponResponse.GetMyRegisteredCouponsResponse> {
        val coupons = couponService.findPublishedByUserId(authentication.id.userId)
        return Response.success(
            CouponResponse.GetMyRegisteredCouponsResponse.from(coupons)
        )
    }
}