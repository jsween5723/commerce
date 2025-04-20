package kr.hhplus.be.server.interfaces.api.coupon

import kr.hhplus.be.server.IntegrationTestSupport
import kr.hhplus.be.server.domain.coupon.CouponFixture
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerTest : IntegrationTestSupport() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `POST api_v1_coupons_{id}_register`() {
        insertTemplate(
            listOf(CouponFixture())
        )
        mockMvc.post("/api/v1/coupons/${longFixture.couponId()}/register") {
            header(AUTHORIZATION, longFixture.userId())
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }

    @Test
    fun `GET api_v1_coupons_me`() {
        mockMvc.get("/api/v1/coupons/me") {
            header(AUTHORIZATION, longFixture.userId())
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }
}