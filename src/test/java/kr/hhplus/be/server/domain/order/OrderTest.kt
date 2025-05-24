package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.AuthException
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.coupon.CouponSnapshotFixture
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
            assertThatCode {
                CreateOrder(Authentication(1L))
            }
//                then
                .doesNotThrowAnyException()
        }
    }

    @Test
    fun `총계를 구할 수 있다`() {
        //given
        val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
        val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
        val order = CreateOrder(Authentication(1L)).toOrder()
        order.addItems(listOf(releaseInfo, releaseInfo2))
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
            val order = CreateOrder(
                Authentication(1L)
            ).toOrder()
            order.addItems(listOf(releaseInfo, releaseInfo2))
            order.addCoupons(listOf(publishedCoupon))

//        when
            val totalPrice = order.totalPrice
//        then
            assertThat(totalPrice).isEqualTo(
                order.orderCoupons.discount(releaseInfo.totalPrice + releaseInfo2.totalPrice)
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
            val order = CreateOrder(Authentication(1L)).toOrder()
            order.addItems(listOf(releaseInfo, releaseInfo2))
//            when
            assertThatCode { order.cancel(Authentication(1L)) }
        }


        @Test
        fun `주문을 생성한 인가정보가 아니면 AuthException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val order = CreateOrder(Authentication(1L)).toOrder()
            order.addItems(listOf(releaseInfo, releaseInfo2))
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
            val order = CreateOrder(Authentication(1L)).toOrder()
            order.addItems(listOf(releaseInfo, releaseInfo2))
//            when
            assertThatCode { order.complete(Authentication(1L)) }
        }

        @Test
        fun `주문을 생성한 인가정보가 아니면 AuthException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val order = CreateOrder(Authentication(1L)).toOrder()
            order.addItems(listOf(releaseInfo, releaseInfo2))
//            when
            assertThatThrownBy { order.complete(Authentication(2)) }.isInstanceOf(AuthException.ForbiddenException::class.java)
        }

        @Test
        fun `PENDING 상태가 아니면 OrderException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val order = CreateOrder(Authentication(1L)).toOrder()
            order.addItems(listOf(releaseInfo, releaseInfo2))
            order.cancel(Authentication(1L))
//            when
            assertThatThrownBy { order.complete(Authentication(1L)) }.isInstanceOf(OrderException.PayOnlyPending::class.java)
        }

        @Test
        fun `이미 PAID 상태면 OrderException을 발생시킨다`() {
            //            given
            val releaseInfo = ProductSnapshotFixture(price = BigDecimal.valueOf(200), quantity = 2)
            val releaseInfo2 = ProductSnapshotFixture(price = BigDecimal.valueOf(500), quantity = 2)
            val order = CreateOrder(Authentication(1L)).toOrder()
            order.addItems(listOf(releaseInfo, releaseInfo2))
            order.addCoupons(listOf())
            order.complete(Authentication(1L))
//            when
            assertThatThrownBy { order.complete(Authentication(1L)) }.isInstanceOf(OrderException.AleadyPaid::class.java)
        }
    }
}