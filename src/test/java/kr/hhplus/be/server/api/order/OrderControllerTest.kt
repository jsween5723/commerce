package kr.hhplus.be.server.api.order

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `POST api_v1_orders`() {
        mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            header(
                HttpHeaders.AUTHORIZATION, "1"
            )
            content = objectMapper.writeValueAsString(
                CreateOrderRequest(
                    orderItems = listOf(
                        CreateOrderRequest.CreateOrderItem(
                            productId = 1, amount = 30
                        )
                    ), registeredCouponIds = listOf(1, 2, 3)
                )
            )
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }

    @Test
    fun `POST api_v1_orders_{id}_pay`() {
        mockMvc.post("/api/v1/orders/1/pay") {
            header(HttpHeaders.AUTHORIZATION, "1")
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }
}