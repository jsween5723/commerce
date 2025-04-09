package kr.hhplus.be.server.domain.product

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ProductServiceTest {
    private val productRepository = mock(ProductRepository::class.java)
    private val productService = ProductService(productRepository)

    @Nested
    inner class `상품을 출고할 수 있다` {

        @Test
        fun `영속계층에 존재하지 않는 상품이 포함되면 ProductException을 발생시킨다`() {
//            given
            val releaseAmount = 2L
            val command = ProductCommand.Release(
                listOf(
                    ProductCommandFixture.ProductIdAndQuantity.create(
                        productId = 1L, quantity = releaseAmount
                    ),
                )
            )
            val targetIds = command.targets.map { it.productId }
            `when`(productRepository.containsIds(targetIds)).thenReturn(false)
            //when
            assertThatThrownBy {
                productService.release(command)
            }.isInstanceOf(ProductException.InvalidProductId::class.java)
        }
    }
}