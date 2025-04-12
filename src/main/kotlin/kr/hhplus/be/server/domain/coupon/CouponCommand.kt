package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.auth.Authentication

class CouponCommand {
    data class Publish(val authentication: Authentication, val couponId: Long) {
        init {
            if (couponId < 1) throw CouponException.CouponNotFound()
        }
    }

    data class Select(val authentication: Authentication, val publishedCouponIds: List<Long>) {
        init {
            publishedCouponIds.forEach { if (it < 1) throw CouponException.CouponNotFound() }
        }
    }
}
