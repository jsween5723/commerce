package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.coupon.CouponCommand
import kr.hhplus.be.server.domain.coupon.CouponService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponFacade(private val couponService: CouponService) {
    fun findPublishedByUserId(authentication: Authentication) =
        couponService.findPublishedByUserId(authentication.id.userId)

    @Transactional
    fun publish(criteria: CouponCriteria.Publish): CouponResult.Publish {
        val publishedCoupon = couponService.publish(CouponCommand.Publish(criteria.authentication, criteria.couponId))
        return CouponResult.Publish(publishedCoupon.id)
    }

}