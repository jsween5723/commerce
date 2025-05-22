package kr.hhplus.be.server.interfaces.external.event

import kr.hhplus.be.server.domain.event.OrderEvent
import kr.hhplus.be.server.domain.order.DataPlatformSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class ExternalOrderEventListener(private val dataPlatformSender: DataPlatformSender) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: OrderEvent.Complete) {
        dataPlatformSender.send(event.order)
    }
}