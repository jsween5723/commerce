package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.IntegrationTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceIntegrationTest : IntegrationTestSupport() {


    @BeforeAll
    fun beforeAll() {
        insertProducts()
        insertRankedProducts()
    }

    @Test
    fun `상품 목록을 조회할 수 있다`() {
        val 상품목록 = 상품목록을_조회한다()
        assertThat(상품목록.size).isEqualTo(MAX_COUNT)
    }

    @Test
    fun `인기상품 목록을 조회할 수 있다`() {
        val 상품목록 = 인기_상품목록을_조회한다(ProductQuery.Ranked())
        assertThat(상품목록.size).isEqualTo(MAX_COUNT)
    }
}