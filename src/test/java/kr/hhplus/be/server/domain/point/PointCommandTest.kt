package kr.hhplus.be.server.domain.point

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PointCommandTest {
    @Nested
    inner class `Charge를 생성할 수 있다` {
        @Test
        fun `amount가 음수면 PointException이 발생한다`() {
            assertThrows<PointException> {
                ChargeFixture(amount = -1L)
            }
        }
    }

    @Nested
    inner class `Use를 생성할 수 있다` {
        @Test
        fun `amount가 음수면 PointException이 발생한다`() {
            assertThrows<PointException> {
                UseFixture(amount = -1L)
            }
        }
    }
}