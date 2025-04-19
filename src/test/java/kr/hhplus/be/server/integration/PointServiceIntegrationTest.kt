package kr.hhplus.be.server.integration

import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.point.PointException
import kr.hhplus.be.server.domain.point.UserPoint
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PointServiceIntegrationTest : IntegrationTestSupport() {


    @Nested
    inner class `포인트를 조회할 수 있다` {

        @Test
        fun `내 포인트를 조회할 수 있다`() {
            val userId = longFixture.userId()
            val point = pointService.findByUserId(UserId(userId))
            assertThat(point.userId.userId).isEqualTo(userId)
        }
    }

    @Nested
    inner class `포인트를 충전할 수 있다` {

        @Test
        fun `포인트를 충전하고 조회하면 더해진다`() {
            val userId = UserId(longFixture.userId())
            val amount = BigDecimal(400.00)
            val expected = 포인트를_조회한다(userId).point + amount
            //when
            포인트를_충전한다(userId, amount)
            val result = 포인트를_조회한다(userId)
            assertThat(result.point.toBigInteger()).isEqualTo(expected.toBigInteger())
        }

        @Test
        fun `충전 시 최대포인트를 넘으면 PointException이 발생한다`() {
            val userId = UserId(longFixture.userId())
            //when
            assertThatThrownBy {
                포인트를_충전한다(userId, UserPoint.MAX + BigDecimal(100))
            }
        }
    }


    @Nested
    inner class `포인트를 사용할 수 있다` {

        @Test
        fun `포인트를 충전한 후 사용하면 감소한다`() {
            val userId = UserId(longFixture.userId())
            val chargeAmount = BigDecimal(400)
            val useAmount = BigDecimal(200)
            val expected = 포인트를_조회한다(userId).point + chargeAmount - useAmount
            포인트를_충전한다(userId, chargeAmount)
            //when
            포인트를_사용한다(userId, useAmount)
            val result = 포인트를_조회한다(userId)
            assertThat(result.point.toBigInteger()).isEqualTo(expected.toBigInteger())
        }

        @Test
        fun `잔액이 부족하면 PointException을 발생시킨다`() {
            val userId = UserId(longFixture.userId())
            val useAmount = BigDecimal(200.00)
            //when
            assertThatThrownBy {
                포인트를_사용한다(userId, useAmount)
            }.isInstanceOf(PointException::class.java)
        }
    }
}