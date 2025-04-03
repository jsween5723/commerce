package kr.hhplus.be.server.api.product

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `GET api_v1_products`() {
        mockMvc.get("/api/v1/products")
            .andExpect { status { isOk() } }
    }

    @Test
    fun `GET api_v1_products_popular`() {
        mockMvc.get("/api/v1/products/popular")
            .andExpect { status { isOk() } }
    }

}