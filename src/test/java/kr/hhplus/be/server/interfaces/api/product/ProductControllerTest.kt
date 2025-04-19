package kr.hhplus.be.server.interfaces.api.product

import kr.hhplus.be.server.integration.IntegrationTestSupport
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest : IntegrationTestSupport() {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `GET api_v1_products`() {
        mockMvc.get("/api/v1/products")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath(
                    "$.data.products",
                    Matchers.instanceOf<List<ProductResponse.GetProductList.ProductDTO>>(List::class.java)
                )
            }
    }

    @Test
    fun `GET api_v1_products_popular`() {
        mockMvc.get("/api/v1/products/popular")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath(
                    "$.data.products",
                    Matchers.instanceOf<List<ProductResponse.GetRankedProductList.RankedProductDTO>>(
                        List::class.java
                    )
                )
            }
    }

}