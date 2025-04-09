package kr.hhplus.be.server.interfaces.product

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(ProductController::class)
class ProductControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `GET api_v1_products`() {
        mockMvc.get("/api/v1/products")
            .andExpect { status { isOk() } }
            .andExpect {
                jsonPath(
                    "$.data.products",
                    Matchers.instanceOf<List<GetProductListResponse.Product>>(List::class.java)
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
                    Matchers.instanceOf<List<GetRankedProductListResponse.RankedProduct>>(List::class.java)
                )
            }
    }

}