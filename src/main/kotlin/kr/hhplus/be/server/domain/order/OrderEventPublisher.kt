package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.event.OrderEvent

interface OrderEventPublisher {
    fun ranked(event: OrderEvent.Ranked)
    fun createStart(event: OrderEvent.CreateStart)
    fun cancel(event: OrderEvent.Cancel)
    fun created(event: OrderEvent.Created)
    fun payStart(event: OrderEvent.PayStart)
    fun complete(event: OrderEvent.Complete)
}