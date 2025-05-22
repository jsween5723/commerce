package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.event.PointEvent

interface PointEventPublisher {
    fun orderUse(event: PointEvent.PaymentOrderUse)
}