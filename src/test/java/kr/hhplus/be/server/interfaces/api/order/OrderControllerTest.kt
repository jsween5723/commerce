package kr.hhplus.be.server.interfaces.api.order

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import kr.hhplus.be.server.IntegrationTestSupport
import kr.hhplus.be.server.LongFixture
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Order
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
class OrderControllerTest : IntegrationTestSupport() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val longFixture = LongFixture()
    val id = longFixture.orderId()
    var orderId = 0L

    @Test
    @Order(1)
    fun `POST api_v1_orders`() {
        val result = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            header(
                HttpHeaders.AUTHORIZATION, id
            )
            content = objectMapper.writeValueAsString(
                OrderRequest.CreateOrder(
                    orderItems = listOf(
                        OrderRequest.CreateOrder.CreateOrderItem(
                            productId = longFixture.orderId(), amount = 30
                        )
                    ), registeredCouponIds = listOf(1, 2, 3)
                )
            )
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }.andReturn().response.contentAsString
        orderId = objectMapper.convertValue<OrderResponse.CreateOrderResponse>(result).id
    }

    @Test
    @Order(2)
    fun `POST api_v1_orders_{id}_pay`() {
        mockMvc.post("/api/v1/orders/${orderId}/pay") {
            header(HttpHeaders.AUTHORIZATION, id)
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }
}