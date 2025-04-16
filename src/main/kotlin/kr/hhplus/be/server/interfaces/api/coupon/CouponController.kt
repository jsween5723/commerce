package kr.hhplus.be.server.interfaces.api.coupon

import kr.hhplus.be.server.application.coupon.CouponCriteria
import kr.hhplus.be.server.application.coupon.CouponFacade
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.interfaces.api.Response
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/coupons")
class CouponController(private val couponFacade: CouponFacade) : CouponSpec {
    @PostMapping("{id}/register")
    override fun register(
        authentication: Authentication,
        @PathVariable id: Long
    ): Response<CouponResponse.RegisterCouponResponse> {
        val published = couponFacade.publish(CouponCriteria.Publish(authentication, id))
        return Response.success(CouponResponse.RegisterCouponResponse(id = published.publishedCouponId))
    }

    @GetMapping("/me")
    override fun getMyRegisteredCoupons(authentication: Authentication): Response<CouponResponse.GetMyRegisteredCouponsResponse> {
        val coupons = couponFacade.findPublishedByUserId(authentication)
        return Response.success(
            CouponResponse.GetMyRegisteredCouponsResponse.from(coupons)
        )
    }
}