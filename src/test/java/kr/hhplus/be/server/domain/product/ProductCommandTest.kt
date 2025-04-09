package kr.hhplus.be.server.domain.product

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ProductCommandTest {
    @Nested
    inner class `Release를 생성할 수 있다` {
        @Test
        fun `productId가 1보다 작은 게 있다면 ProductException을 발생시킨다`() {
            assertThrows<ProductException> {
                ProductCommand.Release(
                    targets = listOf(
                        ProductCommandFixture.ProductIdAndQuantity.create(
                            productId = 0
                        )
                    )
                )
            }
        }

        @Test
        fun `quantity가 1보다 작은 게 있다면 ProductException을 발생시킨다`() {
            assertThrows<ProductException> {
                ProductCommand.Release(
                    targets = listOf(
                        ProductCommandFixture.ProductIdAndQuantity.create(
                            quantity = 0
                        )
                    )
                )
            }
        }

        @Test
        fun `대상이 없으면 ProductException을 발생시킨다`() {
            assertThrows<ProductException> {
                ProductCommand.Release(listOf())
            }
        }

        @Test
        fun `생성할 수 있다`() {
            assertDoesNotThrow {
                ProductCommand.Release(
                    listOf(
                        ProductCommandFixture.ProductIdAndQuantity.create()
                    )
                )
            }
        }
    }
}