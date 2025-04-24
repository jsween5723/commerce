package kr.hhplus.be.server.interfaces.api.product

import kr.hhplus.be.server.domain.product.ProductQuery
import kr.hhplus.be.server.support.IntegrationTestSupport
import org.junit.jupiter.api.Test

class ProductControllerTest : IntegrationTestSupport() {
    @Test
    fun `GET api_v1_products`() {
        상품목록을_조회한다()
    }

    @Test
    fun `GET api_v1_products_popular`() {
        인기_상품목록을_조회한다(ProductQuery.Ranked())
    }

}