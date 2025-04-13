package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.AuthException
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.coupon.CouponSnapshotFixture
import kr.hhplus.be.server.domain.order.payment.Payment
import kr.hhplus.be.server.domain.order.product.ProductSnapshotFixture
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OrderTest {
    @Nested
    inner class `Order를 생성할 수 있다` {
        @Test
        fun `CreateOrder로 Order를 생성할 수 있다`() {
//            given
            val releaseInfo = ProductSnapshotFixture()
            val createOrder = Order.Create(listOf(releaseInfo), Authentication(1L))
//            when
            assertThatCode {
                createOrder.toOrder()
            }
//                then
                .doesNotThrowAnyException()
        }

        @Test
        fun `생성했을 때 paymentStatus는 PENDING이다`() {
            //given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            //when
            val order = createOrder.toOrder()
            //then
            assertThat(order.paymentStatus).isEqualTo(Payment.Status.PENDING)

        }
    }

    @Test
    fun `총계를 구할 수 있다`() {
        //given
        val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
        val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
        val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
        val order = createOrder.toOrder()

//        when
        val totalPrice = order.totalPrice
//        then
        assertThat(totalPrice).isEqualTo(
            releaseInfo.totalPrice + releaseInfo2.totalPrice
        )

    }

    @Nested
    inner class `쿠폰을 적용할 수 있다` {
        @Test
        fun `쿠폰 할인이 적용된다`() {
            //given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val publishedCoupon = CouponSnapshotFixture()
            val createOrder = Order.Create(
                listOf(releaseInfo, releaseInfo2), Authentication(1L), listOf(publishedCoupon)
            )
            val order = createOrder.toOrder()

//        when
            val totalPrice = order.totalPrice
//        then
            assertThat(totalPrice).isEqualTo(
                order.usedCoupons.discount(releaseInfo.totalPrice + releaseInfo2.totalPrice)
            )
        }
    }


    @Nested
    inner class `cancel 메소드를 통해 주문을 취소할 수 있다` {
        @Test
        fun `성공한다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val order = createOrder.toOrder()
//            when
            assertThatCode { order.cancel(Authentication(1L)) }
        }

        @Test
        fun `PAID상태였다면 환급시킬 포인트 금액과 true를 반환한다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val order = createOrder.toOrder()
            order.pay(Authentication(1L))
//            when
            val info = order.cancel(Authentication(1L))
            assertThat(info.pointAmount).isEqualTo(order.payment.amount)
        }

        @Test
        fun `PENDING상태였다면 0과 false를 반환한다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val order = createOrder.toOrder()
            order.pay(Authentication(1L))
//            when
            val info = order.cancel(Authentication(1L))
            assertThat(info.pointAmount).isEqualTo(order.payment.amount)
        }

        @Test
        fun `주문을 생성한 인가정보가 아니면 AuthException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val order = createOrder.toOrder()
//            when
            assertThatThrownBy { order.cancel(Authentication(2)) }.isInstanceOf(AuthException.ForbiddenException::class.java)
        }
    }

    @Nested
    inner class `pay 메소드를 통해 결제처리가 가능하다` {
        @Test
        fun `성공한다`() {
//            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val order = createOrder.toOrder()
//            when
            assertThatCode { order.pay(Authentication(1L)) }
        }

        @Test
        fun `성공한 후 차감시킬 포인트 금액을 반환한다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val order = createOrder.toOrder()
//            when
            val info = order.pay(Authentication(1L))
            assertThat(info.pointAmount).isEqualTo(order.payment.amount)
        }

        @Test
        fun `주문을 생성한 인가정보가 아니면 AuthException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val order = createOrder.toOrder()
//            when
            assertThatThrownBy { order.pay(Authentication(2)) }.isInstanceOf(AuthException.ForbiddenException::class.java)
        }

        @Test
        fun `PENDING 상태가 아니면 OrderException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val order = createOrder.toOrder()
            order.cancel(Authentication(1L))
//            when
            assertThatThrownBy { order.pay(Authentication(1L)) }.isInstanceOf(OrderException.PayOnlyPending::class.java)
        }

        @Test
        fun `이미 PAID 상태면 OrderException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val createOrder = Order.Create(listOf(releaseInfo, releaseInfo2), Authentication(1L))
            val order = createOrder.toOrder()
            order.pay(Authentication(1L))
//            when
            assertThatThrownBy { order.pay(Authentication(1L)) }.isInstanceOf(OrderException.AleadyPaid::class.java)
        }
    }
}