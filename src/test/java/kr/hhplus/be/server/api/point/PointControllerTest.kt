package kr.hhplus.be.server.api.point

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class PointControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `POST api_v1_points_charge`() {
        val contentBody = objectMapper.writeValueAsString(ChargePointRequest(amount = 3956))
        println(contentBody)
        mockMvc.post("/api/v1/points/charge") {
            contentType = MediaType.APPLICATION_JSON
            content = contentBody
            header(HttpHeaders.AUTHORIZATION, "1")
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath(
                    "$.success", `is`(true)
                )
            }
    }

    @Test
    fun `GET api_v1_points_me`() {
        mockMvc.get("/api/v1/points/me") {
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, "1")
        }
            .andExpect {
                status { is2xxSuccessful() }
                jsonPath("$.success", `is`(true))
            }
    }
}