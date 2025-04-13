package kr.hhplus.be.server.domain.order.payment

import kr.hhplus.be.server.domain.auth.AuthException
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.CreateOrder
import kr.hhplus.be.server.domain.order.product.ProductSnapshotFixture
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigDecimal.ZERO

class PaymentTest {
    @Nested
    inner class `생성할 수 있다` {
        @Test
        fun `Order를 통해 생성할 수 있다`() {
//            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200))
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500))
            val order = CreateOrder(listOf(releaseInfo, releaseInfo2), Authentication(1L))
//            when
            assertThatCode { Payment.from(order) }.doesNotThrowAnyException()
        }
    }

    @Nested
    inner class `cancel을 통해 취소처리를 할 수 있다` {
        @Test
        fun `PAID상태였다면 환급시킬 포인트 금액과 true를 반환한다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200))
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500))
            val order = CreateOrder(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val payment = order.payment
            val expected = PaymentInfo.Cancel(payment.amount)
            payment.pay(Authentication(1L))
//            when
            val info = payment.cancel(Authentication(1L))
//then
            assertThat(info).isEqualTo(expected)
        }

        @Test
        fun `PENDING상태였다면 0과 false를 반환한다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200))
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500))
            val order = CreateOrder(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val payment = order.payment
            val expected = PaymentInfo.Cancel(ZERO)
//            when
            val info = payment.cancel(Authentication(1L))
//then
            assertThat(info).isEqualTo(expected)
        }

        @Test
        fun `지불대상자 id와 인가정보가 다르다면 AuthException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200))
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500))
            val order = CreateOrder(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val payment = order.payment
//            when
            assertThatThrownBy {
                payment.cancel(Authentication(2L))
            }.isInstanceOf(AuthException.ForbiddenException::class.java)
        }

        @Test
        fun `이미 CANCELLED 상태라면 PaymentException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200))
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500))
            val order = CreateOrder(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val payment = order.payment
            payment.cancel(Authentication(1L))
//            when
            assertThatThrownBy {
                payment.cancel(Authentication(1L))
            }.isInstanceOf(PaymentException.AlreadyCancelled::class.java)
        }
    }

    @Nested
    inner class `pay를 통해 지불 처리를 할 수 있다` {
        @Test
        fun `성공시 지불대상 금액을 반환한다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200))
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500))
            val order = CreateOrder(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val payment = order.payment
            val expected = PaymentInfo.Pay(payment.amount)
//            when
            val info = payment.pay(Authentication(1L))
//            then
            assertThat(info).isEqualTo(expected)
        }

        @Test
        fun `지불대상자 id와 인가정보가 다르다면 AuthException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200))
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500))
            val order = CreateOrder(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val payment = order.payment
//            when
            assertThatThrownBy {
                payment.pay(Authentication(2L))
            }.isInstanceOf(AuthException.ForbiddenException::class.java)
        }

        @Test
        fun `PAID 상태라면 PaymentException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200))
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500))
            val order = CreateOrder(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val payment = order.payment
            payment.pay(Authentication(1L))
//            when
            assertThatThrownBy {
                payment.pay(Authentication(1L))
            }.isInstanceOf(PaymentException.PayOnlyPendingStatus::class.java)
        }

        @Test
        fun `CANCELLED 상태라면 PaymentException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200))
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500))
            val order = CreateOrder(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val payment = order.payment
            payment.cancel(Authentication(1L))
//            when
            assertThatThrownBy {
                payment.pay(Authentication(1L))
            }.isInstanceOf(PaymentException.PayOnlyPendingStatus::class.java)
        }
    }
}