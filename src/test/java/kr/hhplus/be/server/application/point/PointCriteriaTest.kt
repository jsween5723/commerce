package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.PointException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class PointCriteriaTest {
    @Nested
    inner class `Charge를 생성할 수 있다` {
        @Test
        fun `amount가 음수면 PointException이 발생한다`() {
            assertThrows<PointException> {
                ChargeFixture(amount = -1L)
            }
        }
    }
}