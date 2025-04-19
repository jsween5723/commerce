package kr.hhplus.be.server.integration

import kr.hhplus.be.server.domain.coupon.CouponException
import kr.hhplus.be.server.domain.coupon.CouponService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(
    TestInstance.Lifecycle.PER_CLASS
)
class CouponServiceIntegrationTest : IntegrationTestSupport() {

    @Autowired
    lateinit var couponService2: CouponService

    @BeforeAll
    fun beforeAll() {
        insertCoupons()
    }

    @Nested
    inner class `쿠폰을 발급받을 수 있다` {
        @Test
        fun `존재하는 쿠폰을 발급할 수 있다`() {
            val userId = longFixture.userId()
            val couponId = longFixture.couponId() * 2 + 1
            val 발급된_쿠폰 = 쿠폰을_발급한다(userId, couponId)
            assertThat(발급된_쿠폰).extracting({ it.coupon.id }, { it.userId.userId }).contains(couponId, userId)
        }

        @Test
        fun `존재하지 않는 쿠폰을 발급하면 CouponException이 발생한다`() {
            val userId = longFixture.userId()
            val couponId = MAX_COUNT.plus(2)

            assertThatThrownBy {
                쿠폰을_발급한다(userId, couponId)
            }.isInstanceOf(CouponException::class.java)
        }

        @Test
        fun `재고가 부족한 쿠폰을 발급하면 CouponException이 발생한다`() {
            val userId = longFixture.userId()
            val couponId = longFixture.couponId() * 2
            assertThatThrownBy {
                쿠폰을_발급한다(userId, couponId)
            }.isInstanceOf(CouponException::class.java)
        }
    }

    @Test
    fun `쿠폰을 발급한 후 사용할 수 있다`() {
        val userId = longFixture.userId()
        val couponId = longFixture.couponId() * 2 + 1
        val 발급된_쿠폰 = 쿠폰을_발급한다(userId, couponId)
        val 대상_쿠폰 = 대상_쿠폰_목록을_사용한다(userId, listOf(발급된_쿠폰.id))[0]
        assertThat(대상_쿠폰).extracting({ it.coupon.id }, { it.userId.userId }, { it.used })
            .contains(couponId, userId, true)
    }

    @Test
    fun `쿠폰을 발급한 후 조회할 수 있다`() {
        val userId = longFixture.userId()
        val couponId = longFixture.couponId() * 2 + 1
        val 발급된_쿠폰 = 쿠폰을_발급한다(userId, couponId)
        val 대상_쿠폰 = 사용자의_쿠폰_목록을_조회한다(userId)[0]
        assertThat(대상_쿠폰).extracting({ it.coupon.id }, { it.userId.userId }, { it.used })
            .contains(couponId, userId, false)
    }


}