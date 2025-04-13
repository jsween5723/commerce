package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.product.ProductSnapshotFixture
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CreateTest {

    @Nested
    inner class `CreateOrder를 생성할 수 있다` {
        @Test
        fun `생성할 수 있다`() {
            //given
            val releaseInfo = ProductSnapshotFixture()
            //when
            CreateOrder(listOf(releaseInfo), Authentication(1L))
        }

        @Test
        fun `요소의 수량이 1보다 작으면 OrderException을 발생시킨다`() {
            //given
            val releaseInfo = ProductSnapshotFixture(quantity = 0)
            //when
            assertThatThrownBy { CreateOrder(listOf(releaseInfo), Authentication(1L)) }
        }
    }
}