package kr.hhplus.be.server.interfaces.api.order

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.IntegrationTestSupport
import kr.hhplus.be.server.domain.point.UserPointFixture
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.math.BigDecimal

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest : IntegrationTestSupport() {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `1_POST api_v1_orders`() {
        insertTemplate(listOf(UserPointFixture(point = BigDecimal(10000000))))
        val userId = longFixture.userId()
        val result = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            header(
                HttpHeaders.AUTHORIZATION, userId
            )
            content = objectMapper.writeValueAsString(
                OrderRequest.CreateOrder(
                    orderItems = listOf(
                        OrderRequest.CreateOrder.CreateOrderItem(
                            productId = longFixture.productId(), amount = 1
                        )
                    ),
                    registeredCouponIds = listOf()
                )
            )
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }

    @Test
    fun `2_POST api_v1_orders_{id}_pay`() {
        val userId = longFixture.userId()
        insertTemplate(listOf(UserPointFixture(point = BigDecimal(10000000), userId = userId)))
        val result = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            header(
                HttpHeaders.AUTHORIZATION, userId
            )
            content = objectMapper.writeValueAsString(
                OrderRequest.CreateOrder(
                    orderItems = listOf(
                        OrderRequest.CreateOrder.CreateOrderItem(
                            productId = longFixture.productId(), amount = 1
                        )
                    ),
                    registeredCouponIds = listOf()
                )
            )
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }.andReturn().response.contentAsString
        val orderId = objectMapper.readTree(
            result
        ).get("data").get("id").asLong()
        mockMvc.post("/api/v1/orders/$orderId/pay") {
            header(HttpHeaders.AUTHORIZATION, userId)
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }
}