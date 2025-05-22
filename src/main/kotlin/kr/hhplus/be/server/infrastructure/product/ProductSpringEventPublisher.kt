package kr.hhplus.be.server.infrastructure.product

import kr.hhplus.be.server.domain.event.ProductEvent
import kr.hhplus.be.server.domain.event.ProductEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ProductSpringEventPublisher(private val eventPublisher: ApplicationEventPublisher) : ProductEventPublisher {
    override fun orderDeduct(event: ProductEvent.OrderDeduct) {
        eventPublisher.publishEvent(event)
    }
}