package kr.hhplus.be.server.interfaces.point.event

import kr.hhplus.be.server.domain.event.OrderEvent
import kr.hhplus.be.server.domain.event.PointEvent
import kr.hhplus.be.server.domain.event.PointEventPublisher
import kr.hhplus.be.server.domain.point.PointCommand
import kr.hhplus.be.server.domain.point.PointService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class PointOrderEventListener(private val pointService: PointService, private val eventPublisher: PointEventPublisher) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    fun handle(event: OrderEvent.PayStart) {
        pointService.use(
            PointCommand.Use(
                amount = event.amount,
                userId = event.authentication.id,
                authentication = event.authentication
            )
        )
        eventPublisher.orderUse(
            PointEvent.PaymentOrderUse(
                orderId = event.orderId,
                amount = event.amount, authentication = event.authentication,
            )
        )
    }
}