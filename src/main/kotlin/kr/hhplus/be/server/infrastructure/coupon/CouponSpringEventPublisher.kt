package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.event.CouponEvent
import kr.hhplus.be.server.domain.coupon.CouponEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class CouponSpringEventPublisher(private val publisher: ApplicationEventPublisher) : CouponEventPublisher {
    override fun used(event: CouponEvent.OrderUsed) {
        publisher.publishEvent(event)
    }
}