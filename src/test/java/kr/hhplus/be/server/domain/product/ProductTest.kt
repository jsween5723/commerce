package kr.hhplus.be.server.domain.product

import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class ProductTest {
    @Nested
    inner class `Product를 생성할 수 있다` {
        @Test
        fun `재고량이 음수면 ProductException이 발생한다`() {
            assertThrows<ProductException> {
                ProductFixture(stockNumber = -1)
            }
        }

        @Test
        fun `가격이 음수면 ProductException이 발생한다`() {
            assertThrows<ProductException> {
                ProductFixture(price = BigDecimal.valueOf(-1))
            }
        }

        @Test
        fun `생성할 수 있다`() {
            assertDoesNotThrow { ProductFixture() }
        }
    }

    @Nested
    inner class `출고할 수 있다` {
        @Test
        fun `amount만큼 stockNumber가 감소한다`() {
            //given
            val before = 100L
            val amount = 50L
            val product = ProductFixture(stockNumber = before)
            //when
            product.release(amount)

            //then
            assertEquals(before - amount, product.stockNumber)
        }

        @Test
        fun `출고량을 포함해 ReleaseVO를 생성 및 반환한다`() {
            //given
            val before = 100L
            val amount = 50L
            val product = ProductFixture(stockNumber = before)
            val expected = product.ReleaseInfo(amount)
            //when
            val releaseVO = product.release(amount)

            //then
            assertEquals(expected, releaseVO)
        }

        @Test
        fun `출고량이 재고보다 많으면 ProductException을 발생시킨다`() {
            //given
            val before = 50L
            val amount = 100L
            val product = ProductFixture(stockNumber = before)
            //when
            assertThrows<ProductException> {
                product.release(amount)
            }
        }

        @Test
        fun `출고량이 1보다 작으면 ProductException을 발생시킨다`() {
            //given
            val before = 50L
            val amount = 0L
            val product = ProductFixture(stockNumber = before)
            //when
            assertThrows<ProductException> {
                product.release(amount)
            }
        }
    }

    @Nested
    inner class `적재할 수 있다` {
        @Test
        fun `amount만큼 stockNumber가 증가한다`() {
            //given
            val before = 100L
            val amount = 50L
            val product = ProductFixture(stockNumber = before)
            //when
            product.restock(amount)

            //then
            assertEquals(before + amount, product.stockNumber)
        }

        @Test
        fun `적재량이 1보다 작으면 ProductException을 발생시킨다`() {
            //given
            val before = 50L
            val amount = 0L
            val product = ProductFixture(stockNumber = before)
            //when
            assertThrows<ProductException> {
                product.restock(amount)
            }
        }
    }
}