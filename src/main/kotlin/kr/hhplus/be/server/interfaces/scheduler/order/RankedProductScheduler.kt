package kr.hhplus.be.server.interfaces.scheduler.order

import kr.hhplus.be.server.domain.order.OrderService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class RankedProductScheduler(private val orderService: OrderService) {
    @Scheduled(cron = "0 0 12 * * *")
    @Transactional
    fun execute() {
        orderService.createRankedProductByDate(LocalDate.now(), 3)
    }
}