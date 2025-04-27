package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.support.IntegrationTestSupport
import kr.hhplus.be.server.support.concurrentlyRun
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductIntegrationTest : IntegrationTestSupport() {

    @Autowired
    lateinit var productService: ProductService

    @Autowired
    lateinit var productRepository: JpaRepository<Product, Long>

    @Test
    fun `재고 차감 시 동시에 시도해도 모두 처리된다`() {
//        given
        val previousStock = 20L
        val quantity = 2L
        val productId = 1L
        insertTemplate(listOf(ProductFixture(stockNumber = previousStock)))
        val releaseCommand =
            ProductCommand.Release(targets = listOf(ProductCommand.ProductIdAndQuantity(productId, quantity)))
        concurrentlyRun(arrayOf({ productService.release(releaseCommand) }, { productService.release(releaseCommand) }))
        val result = productRepository.findById(1)
        assertThat(result.get().stockNumber).isEqualTo(previousStock - (quantity * 2))
    }

    @Test
    fun `재고 증가 시 동시에 시도해도 모두 처리된다`() {
//        given
        val previousStock = 20L
        val quantity = 2L
        val productId = 1L
        insertTemplate(listOf(ProductFixture(stockNumber = previousStock)))
        val command =
            ProductCommand.Restock(targets = listOf(ProductCommand.ProductIdAndQuantity(productId, quantity)))
        concurrentlyRun(arrayOf({ productService.restock(command) }, { productService.restock(command) }))
        val result = productRepository.findById(1)
        assertThat(result.get().stockNumber).isEqualTo(previousStock + (quantity * 2))
    }

    @Test
    fun `재고 증가와 차감이 동시에 발생해도 모두 처리된다`() {
//        given
        val previousStock = 20L
        val releaseQuantity = 2L
        val restockQuantity = 4L
        val productId = 1L
        insertTemplate(listOf(ProductFixture(stockNumber = previousStock)))
        val restockCommand =
            ProductCommand.Restock(targets = listOf(ProductCommand.ProductIdAndQuantity(productId, restockQuantity)))
        val releaseCommand =
            ProductCommand.Release(targets = listOf(ProductCommand.ProductIdAndQuantity(productId, releaseQuantity)))
        concurrentlyRun(arrayOf({ productService.restock(restockCommand) }, { productService.release(releaseCommand) }))
        val result = productRepository.findById(1)
        assertThat(result.get().stockNumber).isEqualTo(previousStock + restockQuantity - releaseQuantity)
    }
}