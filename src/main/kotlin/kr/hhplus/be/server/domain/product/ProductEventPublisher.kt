package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.event.ProductEvent

interface ProductEventPublisher {
    fun orderDeduct(event: ProductEvent.OrderDeduct)
}