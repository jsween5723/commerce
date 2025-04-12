package kr.hhplus.be.server.interfaces.scheduler

import kr.hhplus.be.server.application.product.ProductFacade
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class RankedProductScheduler(private val productFacade: ProductFacade) {
    @Scheduled(cron = "0 0 12 * * *")
    @Transactional
    fun execute() {
        productFacade.createRankedProductByDate(LocalDate.now(), 3)
    }
}