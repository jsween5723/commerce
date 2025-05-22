package kr.hhplus.be.server.interfaces.api.coupon

import kr.hhplus.be.server.IntegrationTestSupport
import kr.hhplus.be.server.domain.coupon.CouponFixture
import org.junit.jupiter.api.Test

class CouponControllerTest : IntegrationTestSupport() {

    @Test
    fun `POST api_v1_coupons_{id}_register`() {
        val userId = idGenerator.userId()
        val couponId = idGenerator.couponId()
        insertTemplate(
            listOf(CouponFixture())
        )
        사용자에게_쿠폰을_발급한다(userId = userId, couponId = couponId)
    }

    @Test
    fun `GET api_v1_coupons_me`() {
        사용자의_쿠폰_목록을_조회한다(idGenerator.userId())
    }
}