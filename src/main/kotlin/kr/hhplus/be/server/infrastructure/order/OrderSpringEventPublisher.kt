package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.event.OrderEvent
import kr.hhplus.be.server.domain.event.OrderEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class OrderSpringEventPublisher(private val publisher: ApplicationEventPublisher) : OrderEventPublisher {
    override fun ranked(event: OrderEvent.Ranked) {
        publisher.publishEvent(event)
    }

    override fun createStart(event: OrderEvent.CreateStart) {
        publisher.publishEvent(event)
    }

    override fun cancel(event: OrderEvent.Cancel) {
        publisher.publishEvent(event)
    }

    override fun created(event: OrderEvent.Created) {
        publisher.publishEvent(event)
    }
    
    override fun payStart(event: OrderEvent.PayStart) {
        publisher.publishEvent(event)
    }

    override fun complete(event: OrderEvent.Complete) {
        publisher.publishEvent(event)
    }
}