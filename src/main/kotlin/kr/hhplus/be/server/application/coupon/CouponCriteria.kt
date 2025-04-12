package kr.hhplus.be.server.application.coupon

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.coupon.CouponException

class CouponCriteria {
    data class Publish(val authentication: Authentication, val couponId: Long) {
        init {
            if (couponId < 1) throw CouponException.CouponNotFound()
        }
    }
}