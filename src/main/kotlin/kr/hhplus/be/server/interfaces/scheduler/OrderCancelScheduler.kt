package kr.hhplus.be.server.interfaces.scheduler


import kr.hhplus.be.server.application.order.OrderCriteria
import kr.hhplus.be.server.application.order.OrderFacade
import kr.hhplus.be.server.domain.auth.Authentication
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class OrderCancelScheduler(
    private val orderFacade: OrderFacade, facade: OrderFacade
) {
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    fun execute() {
        orderFacade.cancelByDate(
            OrderCriteria.CancelBy(
                pendingTime = LocalDateTime.now().minusDays(1),
                authentication = Authentication(userId = Long.MAX_VALUE, isSuper = true)
            )
        )
    }
}