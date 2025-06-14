package io.hhplus.tdd.point

import kr.hhplus.be.server.domain.auth.AuthException
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.point.PointException
import kr.hhplus.be.server.domain.point.UserPoint
import kr.hhplus.be.server.domain.point.UserPointFixture
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class UserPointTest {
    @Nested
    inner class `UserPoint를 생성할 수 있다` {
        @Test
        fun `생성 시 point가 음수일 때 PointException이 발생한다`() {
            assertThrows<PointException> {
                UserPointFixture(point = BigDecimal.valueOf(-1))
            }
        }
    }

    @Nested
    inner class `포인트를 충전할 수 있다` {
        @Test
        fun `포인트 충전시 MAX를 초과하면 PointException이 발생한다`() {
            val userPoint = UserPointFixture(userId = 1L)
            val authentication = Authentication(1L)
            assertThrows<PointException> {
                userPoint.charge(UserPoint.MAX, authentication)
            }

        }


        @Test
        fun `포인트 충전시 amount가 음수면 PointException이 발생한다`() {
            val userPoint = UserPointFixture(userId = 1L)
            val authentication = Authentication(1L)
            assertThrows<PointException> {
                userPoint.charge(BigDecimal.valueOf(-1), authentication)
            }
        }

        @Test
        fun `포인트 충전 시 authentication이 자신이 아니면 AuthException이 발생한다`() {
            val userPoint = UserPointFixture(userId = 1L)
            val authentication = Authentication(2L)
            assertThatThrownBy {
                userPoint.charge(BigDecimal.valueOf(1), authentication)
            }.isInstanceOf(AuthException.ForbiddenException::class.java)
        }

        @Test
        fun `포인트 충전 시 authentication이 자신이 아니어도 isSuper가 true면 성공한다`() {
            val userPoint = UserPointFixture(userId = 1L)
            val authentication = Authentication(2L, true)
            assertThatCode { userPoint.charge(BigDecimal.valueOf(1), authentication) }.doesNotThrowAnyException()
        }

        @Test
        fun `포인트 충전시 잔액에 추가된다`() {
            //given
            val userPoint = UserPointFixture(userId = 1L)
            val authentication = Authentication(1L)
            val before = userPoint.point
            val amount = BigDecimal.valueOf(100L)
            //when
            userPoint.charge(amount, authentication)
            Assertions.assertEquals(
                before + amount, userPoint.point
            )
        }
    }

    @Nested
    inner class `포인트 사용 케이스` {
        @Test
        fun `포인트 사용시 값만큼 잔액에서 뺀다`() {
            //given
            val userPoint = UserPointFixture(userId = 1L)
            val authentication = Authentication(1L)
            val before = userPoint.point
            val usedPoint = BigDecimal.valueOf(100L)
            //when
            userPoint.use(usedPoint, authentication)
            Assertions.assertEquals(
                before - usedPoint, userPoint.point
            )
        }

        @Test
        fun `포인트 사용 시 잔액을 초과하면 PointException이 발생한다`() {
            val userPoint = UserPointFixture(userId = 1L)
            val authentication = Authentication(1L)
            val before = userPoint.point
            assertThrows<PointException> {
                userPoint.use(before + BigDecimal.ONE, authentication)
            }
        }

        @Test
        fun `포인트 사용 시 amount가 음수면 PointException이 발생한다`() {
            val userPoint = UserPointFixture(userId = 1L)
            val authentication = Authentication(1L)
            assertThrows<PointException> {
                userPoint.use(BigDecimal.valueOf(-2L), authentication)
            }
        }

        @Test
        fun `포인트 사용 시 authentication이 자신이 아니면 AuthException이 발생한다`() {
            val userPoint = UserPointFixture(userId = 1L)
            val authentication = Authentication(2L)
            assertThatThrownBy {
                userPoint.use(BigDecimal.valueOf(1), authentication)
            }.isInstanceOf(AuthException.ForbiddenException::class.java)
        }

        @Test
        fun `포인트 사용 시 authentication이 자신이 아니어도 isSuper가 true면 성공한다`() {
            val userPoint = UserPointFixture(userId = 1L)
            val authentication = Authentication(2L, true)
            assertThatCode { userPoint.use(BigDecimal.valueOf(1), authentication) }.doesNotThrowAnyException()
        }
    }

}