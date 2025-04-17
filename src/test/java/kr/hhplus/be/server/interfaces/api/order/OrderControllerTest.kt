package kr.hhplus.be.server.interfaces.api.order

import com.fasterxml.jackson.databind.ObjectMapper
import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.point.UserPoint
import kr.hhplus.be.server.integration.IntegrationTestSupport
import org.hamcrest.Matchers.`is`
import org.instancio.Instancio
import org.instancio.Select.field
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
    val userId = longFixture.userId()
    val orderId = longFixture.orderId()

    @Test
    fun `1_POST api_v1_orders`() {
        val result = mockMvc.post("/api/v1/orders") {
            contentType = MediaType.APPLICATION_JSON
            header(
                HttpHeaders.AUTHORIZATION, userId
            )
            content = objectMapper.writeValueAsString(
                OrderRequest.CreateOrder(
                    orderItems = listOf(
                        OrderRequest.CreateOrder.CreateOrderItem(
                            productId = longFixture.productId(), amount = 30
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
        insertTemplate(
            listOf(
                Instancio.of(UserPoint::class.java)
                    .supply(field("userId")) { gen -> UserId(userId) }
                    .supply(field("point")) { gen -> gen.longRange(10000, 10000000).toBigDecimal() }
                    .create())
        )
        mockMvc.post("/api/v1/orders/1/pay") {
            header(HttpHeaders.AUTHORIZATION, userId)
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }
}