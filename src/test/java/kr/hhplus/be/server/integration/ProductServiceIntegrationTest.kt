package kr.hhplus.be.server.integration

import kr.hhplus.be.server.domain.OrderBy
import kr.hhplus.be.server.domain.product.ProductQuery
import kr.hhplus.be.server.domain.product.RankedProductField
import kr.hhplus.be.server.domain.product.RankedProductOrderBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceIntegrationTest : IntegrationTestSupport() {


    @Test
    fun `상품 목록을 조회할 수 있다`() {
        val 상품목록 = 상품목록을_조회한다()
    }

    @Test
    fun `인기상품 목록을 조회할 수 있다`() {
        val 상품목록 = 인기_상품목록을_조회한다(
            ProductQuery.Ranked(
                day = LocalDate.now(),
                orders = setOf(
                    RankedProductOrderBy(RankedProductField.TOTAL_INCOME, OrderBy.Type.DESC),
                    RankedProductOrderBy(RankedProductField.TOTAL_SELLING_COUNT, OrderBy.Type.DESC),
                )
            )
        )
    }
}