package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.product.ProductFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CreateOrderTest {
    @Nested
    inner class `CreateOrder를 생성할 수 있다` {
        @Test
        fun `생성할 수 있다`() {
            //given
            val releaseInfo = ProductFixture().ReleaseInfo(2)
            //when
            CreateOrder.from(listOf(releaseInfo), Authentication(1L))
        }

        @Test
        fun `요소의 수량이 1보다 작으면 OrderException을 발생시킨다`() {
            //given
            val releaseInfo = ProductFixture().ReleaseInfo(0)
            //when
            assertThatThrownBy { CreateOrder.from(listOf(releaseInfo), Authentication(1L)) }
        }

        @Test
        fun `요소의 정보가 생성 시점의 Product의 정보와 일치한다`() {
            //given
            val releaseInfo = ProductFixture().ReleaseInfo(2)
            //when
            val createOrder = CreateOrder.from(listOf(releaseInfo), Authentication(1L))
            val createOrderItem = createOrder.receipt.items[0]
            assertThat(createOrderItem).extracting({ it.name }, { it.priceOfOne })
                .containsExactly(
                    releaseInfo.product.name,
                    releaseInfo.product.price,
                )
        }
    }
//    외부에 노출되지 않아 발생할 수 없는 케이스입니다.
//    @Test
//    fun `요소의 정보가 Product의 정보와 일치하지 않으면 validate시 OrderItemInfoValidateFail을 발생시킨다`() {
//        //given
//    }

}