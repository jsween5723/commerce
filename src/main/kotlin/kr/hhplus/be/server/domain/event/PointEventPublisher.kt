package kr.hhplus.be.server.domain.event

interface PointEventPublisher {
    fun orderUse(event: PointEvent.PaymentOrderUse)
}