package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.IntegrationTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceIntegrationTest : IntegrationTestSupport() {
    @Autowired
    lateinit var productService: ProductService

    @BeforeAll
    fun beforeAll() {
        insertProducts()
    }

    @Nested
    inner class `상품을 출고할 수 있다` {

        @Test
        fun `상품을 출고 시키면 Product_Release_VO를 반환한다`() {
            val releaseAmount = 2L
            val productId = idGenerator.productId()
            val command = ProductCommand.Release(
                listOf(
                    ProductCommandFixture.ProductIdAndQuantity.create(
                        productId = productId, quantity = releaseAmount
                    ),
                )
            )
            val targetIds = command.targets.map { it.productId }
            val expected = productRepository.findByIdOrNull(productId)!!.release(releaseAmount)
            //when
            val released = productService.release(command)
            assertThat(released[0]).extracting({ it.quantity }, { it.product.id })
                .contains(expected.quantity, expected.product.id)
        }

        @Test
        fun `상품을 출고시키면 값이 감소한다`() {
            val targets = IntRange(1, MAX_COUNT.toInt()).map {
                Instancio.of(ProductCommand.ProductIdAndQuantity::class.java)
                    .supply(field("productId")) { gen ->
                        it
                    }
                    .generate(field("quantity")) { gen ->
                        gen.longs().min(1).max(10)
                    }
                    .create()
            }.toList()
            val command = ProductCommand.Release(targets)
            val set = targets.map { it.productId }.toSet()
            val expected = productRepository.findAll().filter { set.contains(it.id) }
                .mapIndexed { index, it -> it.stockNumber - targets[index].quantity }.toSet()
            //when
            productService.release(command)
            val result = productRepository.findAll().filter { set.contains(it.id) }.map { it.stockNumber }.toSet()
            assertThat(result).containsAll(expected)
        }

        @Test
        fun `존재하지 않는 상품이라면 ProductException을 발생시킨다`() {
            val targets = IntRange(1, MAX_COUNT.toInt()).map {
                Instancio.of(ProductCommand.ProductIdAndQuantity::class.java)
                    .supply(field("productId")) { gen ->
                        it
                    }
                    .generate(field("quantity")) { gen ->
                        gen.longs().min(1).max(10)
                    }
                    .create()
            }.toMutableList()
            targets.add(ProductCommand.ProductIdAndQuantity(MAX_COUNT.plus(2), 10))
            val command = ProductCommand.Release(targets)
            //when
            assertThatThrownBy { productService.release(command) }.isInstanceOf(ProductException::class.java)
        }

        @Test
        fun `수량이 부족하면 ProductException을 발생시킨다`() {
            val targets = IntRange(1, MAX_COUNT.toInt()).map {
                Instancio.of(ProductCommand.ProductIdAndQuantity::class.java)
                    .supply(field("productId")) { gen ->
                        it
                    }
                    .generate(field("quantity")) { gen ->
                        gen.longs().min(1).max(10)
                    }
                    .create()
            }.toMutableList()
            targets.add(ProductCommand.ProductIdAndQuantity(MAX_COUNT.plus(2), MAX_STOCK_NUMBER + 2))
            val command = ProductCommand.Release(targets)
            //when
            assertThatThrownBy { productService.release(command) }.isInstanceOf(ProductException::class.java)
        }
    }

    @Nested
    inner class `상품을 적재할 수 있다` {

        @Test
        fun `상품을 적재 시키고 조회하면 값이 증가한다`() {
            val targets = IntRange(1, MAX_COUNT.toInt()).map {
                Instancio.of(ProductCommand.ProductIdAndQuantity::class.java)
                    .supply(field("productId")) { gen ->
                        it
                    }
                    .generate(field("quantity")) { gen ->
                        gen.longs().min(1).max(10)
                    }
                    .create()
            }.toList()
            val command = ProductCommand.Restock(targets)
            val set = targets.map { it.productId }.toSet()
            val expected = productRepository.findAll().filter { set.contains(it.id) }
                .mapIndexed { index, it -> it.stockNumber + targets[index].quantity }.toSet()
            //when
            productService.restock(command)
            val result = productRepository.findAll().filter { set.contains(it.id) }.map { it.stockNumber }.toSet()
            assertThat(result).containsAll(expected)
        }

        @Test
        fun `일부가 존재하지 않는 상품이라면 ProductException을 발생시킨다`() {
            val targets = IntRange(1, MAX_COUNT.toInt()).map {
                Instancio.of(ProductCommand.ProductIdAndQuantity::class.java)
                    .supply(field("productId")) { gen ->
                        it
                    }
                    .generate(field("quantity")) { gen ->
                        gen.longs().min(1).max(10)
                    }
                    .create()
            }.toMutableList()
            targets.add(ProductCommand.ProductIdAndQuantity(MAX_COUNT.plus(2), 10))
            val command = ProductCommand.Restock(targets)
            //when
            assertThatThrownBy { productService.restock(command) }.isInstanceOf(ProductException::class.java)
        }
    }
}