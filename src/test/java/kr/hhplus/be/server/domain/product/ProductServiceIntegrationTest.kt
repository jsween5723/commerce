package kr.hhplus.be.server.domain.product

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ProductServiceIntegrationTest {
//    private val productRepository = mock(ProductRepository::class.java)
//    private val productService = ProductService(productRepository)

    @Nested
    inner class `상품을 출고할 수 있다` {

        @Test
        fun `상품을 출고 시키면 Product_Release_VO를 반환한다`() {
//            TODO: ID를 기반으로 mapping을 수행하기에 영속계층과 연계 해야하며 단위기능 자체는 Product 테스트로 충족가능
            //given
//            val releaseAmount = 2L
//            val command = ProductCommand.Release(
//                listOf(
//                    ProductCommandFixture.ProductIdAndQuantity.create(
//                        productId = 1L, quantity = releaseAmount
//                    ),
//                )
//            )
//            val targetIds = command.targets.map { it.productId }
//            val product = ProductFixture(id = 1L, stockNumber = 10)
//            val expected = listOf(product.ReleaseVO(quantity = releaseAmount))
//            `when`(productRepository.findByIds(targetIds)).thenReturn(listOf(product))
//            //when
//            val released = productService.release(command)
//            assertThat(released).isEqualTo(expected)
        }
    }
}