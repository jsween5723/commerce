package kr.hhplus.be.server.domain.order

import java.time.LocalDate
import java.time.LocalDateTime

class OrderQuery {
    data class ForStatistics(val status: Order.Status, val from: LocalDate, val to: LocalDate) {
        init {
            if (from.isAfter(to)) throw OrderException.FromIsAfterTo()
        }
    }

    data class ForCancelSchedule(val status: Order.Status, val until: LocalDateTime)
}
