package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.auth.Authentication
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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
            val coupon = PublishedCouponFixture(expireAt = LocalDateTime.now().minusDays(1))
            val now = LocalDateTime.now()
            assertThatThrownBy {
                coupon.use(
                    now, Authentication(coupon.userId.userId)
                )
            }.isInstanceOf(CouponException.CouponExpired::class.java)
        }
    }

    @Nested
    inner class Deuse {
        @Test
        fun `사용취소하면 usedAt이 null이 된다`() {
//            given
            val coupon = PublishedCouponFixture()
            val now = LocalDateTime.now()
            coupon.use(
                now, Authentication(coupon.userId.userId)
            )
//            when
            assertThatCode {
                coupon.deuse()
            }.doesNotThrowAnyException()
            assertThat(coupon.used).isEqualTo(false)
            assertThat(coupon.usedAt).isNull()
        }
    }
}