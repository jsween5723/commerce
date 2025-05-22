package kr.hhplus.be.server.domain.event

interface ProductEventPublisher {
    fun orderDeduct(event: ProductEvent.OrderDeduct)
}