package kr.hhplus.be.server.integration

import kr.hhplus.be.server.IntegrationTestSupport
import kr.hhplus.be.server.domain.coupon.CouponException
import kr.hhplus.be.server.domain.coupon.CouponFixture
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(
    TestInstance.Lifecycle.PER_CLASS
)
class CouponServiceIntegrationTest : IntegrationTestSupport() {
    @Test
    fun `존재하는 쿠폰을 발급할 수 있다`() {
        insertTemplate(listOf(CouponFixture(stock = 20L)))
        val userId = longFixture.userId()
        val couponId = longFixture.couponId()
        val 발급된_쿠폰 = 쿠폰을_발급한다(userId, couponId)
        assertThat(발급된_쿠폰).extracting({ it.coupon.id }, { it.userId.userId }).contains(couponId, userId)
    }

    @Test
    fun `1 존재하지 않는 쿠폰을 발급하면 CouponException이 발생한다`() {
        val userId = longFixture.userId()
        val couponId = longFixture.couponId()
        assertThatThrownBy {
            쿠폰을_발급한다(userId, couponId)
        }.isInstanceOf(CouponException::class.java)
    }

    @Test
    fun `재고가 부족한 쿠폰을 발급하면 CouponException이 발생한다`() {
        insertTemplate(listOf(CouponFixture(stock = 0)))
        val userId = longFixture.userId()
        val couponId = longFixture.couponId()
        assertThatThrownBy {
            쿠폰을_발급한다(userId, couponId)
        }.isInstanceOf(CouponException::class.java)
    }

    @Test
    fun `쿠폰을 발급한 후 조회할 수 있다`() {
        insertTemplate(listOf(CouponFixture(stock = 20L)))
        val userId = longFixture.userId()
        val couponId = longFixture.couponId()
        val 발급된_쿠폰 = 쿠폰을_발급한다(userId, couponId)
        val 대상_쿠폰 = 사용자의_쿠폰_목록을_조회한다(userId)[0]
        assertThat(대상_쿠폰).extracting({ it.coupon.id }, { it.userId.userId }, { it.used })
            .contains(couponId, userId, false)
    }


}