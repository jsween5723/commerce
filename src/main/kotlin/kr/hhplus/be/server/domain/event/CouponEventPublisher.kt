package kr.hhplus.be.server.domain.event

interface CouponEventPublisher {
    fun used(event: CouponEvent.OrderUsed)
}


