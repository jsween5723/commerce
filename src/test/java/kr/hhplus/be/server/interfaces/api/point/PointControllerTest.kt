package kr.hhplus.be.server.interfaces.api.point

import kr.hhplus.be.server.IntegrationTestSupport
import org.junit.jupiter.api.Test

class PointControllerTest : IntegrationTestSupport() {


    @Test
    fun `POST api_v1_points_charge`() {
        val userId = idGenerator.userId()
        포인트를_충전한다(userId = userId, amount = 100_000_000.toBigDecimal())
    }

    @Test
    fun `GET api_v1_points_me`() {
        val userId = idGenerator.userId()
        포인트를_조회한다(userId)
    }
}