package kr.hhplus.be.server.integration

import kr.hhplus.be.server.domain.coupon.CouponErrorCode
import kr.hhplus.be.server.domain.coupon.CouponFixture
import kr.hhplus.be.server.interfaces.api.coupon.사용자에게_쿠폰을_발급한다
import kr.hhplus.be.server.interfaces.api.coupon.사용자의_쿠폰_목록을_조회한다
import kr.hhplus.be.server.support.IntegrationTestSupport
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CouponIntegrationTest : IntegrationTestSupport() {
    @Test
    fun `존재하지 않는 쿠폰을 발급하면 CouponException이 발생한다`() {
        val userId = idGenerator.userId()
        val couponId = 1L
        val result = 사용자에게_쿠폰을_발급한다(userId, couponId)
        assertThat(result.error?.code).contains(CouponErrorCode.COUPON_NOT_FOUND.name)
    }

    @Test
    fun `쿠폰을 발급하면 내 쿠폰 목록을 조회할 때 확인할 수 있다`() {
        insertTemplate(listOf(CouponFixture(stock = 20L)))
        val userId = idGenerator.userId()
        val couponId = 1L
        val 발급된_쿠폰 = 사용자에게_쿠폰을_발급한다(userId, couponId)
        val 대상_쿠폰 =
            사용자의_쿠폰_목록을_조회한다(userId).data!!.coupons[0]
        assertThat(대상_쿠폰).extracting({ it.coupon.id }, { it.userId })
            .contains(couponId, userId)
    }

    @Test
    fun `동시에 여러 쿠폰을 발급하면 개수가 정상적으로 적용된다`() {
        insertTemplate(listOf(CouponFixture(stock = 3L)))
        val userId = idGenerator.userId()
        val couponId = idGenerator.couponId()
    }
}