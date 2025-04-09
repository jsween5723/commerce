package kr.hhplus.be.server.interfaces.coupon

import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(CouponController::class)
class CouponControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `POST api_v1_coupons_{id}_register`() {
        mockMvc.post("/api/v1/coupons/1/register") {
            header(AUTHORIZATION, "1")
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }

    @Test
    fun `GET api_v1_coupons_me`() {
        mockMvc.get("/api/v1/coupons/me") {
            header(AUTHORIZATION, "1")
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }
}