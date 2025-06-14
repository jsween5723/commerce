package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.auth.Authentication
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

class PublishedCouponTest {
    @Nested
    inner class Use {
        @Test
        fun `사용하면 usedAt에 시간이 기록된다`() {
            val coupon = PublishedCouponFixture()
            val now = LocalDateTime.now()
            assertThatCode {
                coupon.use(
                    now, Authentication(coupon.userId.userId)
                )
            }.doesNotThrowAnyException()
            assertThat(coupon.used).isEqualTo(true)
            assertThat(coupon.usedAt).isEqualTo(now)
        }

        @Test
        fun `만료됐다면 CouponException을 발생시킨다`() {
            val coupon = PublishedCouponFixture(
                now = LocalDateTime.now(),
                coupon = CouponFixture(expireDuration = Duration.ZERO)
            )
            val now = LocalDateTime.now().plusDays(1)
            assertThatThrownBy {
                coupon.use(
                    now, Authentication(coupon.userId.userId)
                )
            }.isInstanceOf(CouponException.CouponExpired::class.java)
        }
    }
}