package kr.hhplus.be.server.domain.order

import java.time.LocalDate

class OrderQuery {
    data class ForStatistics(val status: Order.Status, val from: LocalDate, val to: LocalDate) {
        init {
            if (from.isAfter(to)) throw OrderException.FromIsAfterTo()
        }
    }
}
