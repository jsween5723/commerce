package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.event.CouponEvent

interface CouponEventPublisher {
    fun used(event: CouponEvent.OrderUsed)
}


