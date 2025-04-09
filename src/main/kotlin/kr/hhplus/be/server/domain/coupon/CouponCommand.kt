package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.auth.Authentication

class CouponCommand {
    data class Publish(val authentication: Authentication, val couponId: Long) {
        init {
            if (couponId < 1) throw CouponException.CouponNotFound()
        }
    }
}
