package kr.hhplus.be.server.domain.order

import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

class OrderQueryTest {
    @Nested
    inner class `ForStatistics를 생성할 수 있다` {
        @Test
        fun `생성할 수 있다`() {
//            given
            val to = LocalDate.now()
            val from = LocalDate.now().minusDays(3)
//            when
            assertThatCode {
                OrderQuery.ForStatistics(status = Order.Status.COMPLETED, from = from, to = to)
            }.doesNotThrowAnyException()
        }

        @Test
        fun `from이 to보다 뒤면 OrderException을 발생시킨다`() {
//            given
            val from = LocalDate.now()
            val to = LocalDate.now().minusDays(3)
//            when
            assertThatThrownBy {
                OrderQuery.ForStatistics(status = Order.Status.COMPLETED, from = from, to = to)
            }.isInstanceOf(OrderException.FromIsAfterTo::class.java)
        }
    }
}