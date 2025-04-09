package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.auth.UserId
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class CouponTest {
    @Nested
    inner class `발급할 수 있다` {
        @Test
        fun `발급하고 PublishedCoupon을 반환하고 재고가 1 줄어든다`() {
            val coupon = CouponFixture()
            val beforeStock = coupon.stock
            val published = coupon.publish(UserId(1), LocalDateTime.now())
            assertThat(published).extracting({ it.coupon }, { it.userId })
                .containsExactly(coupon, UserId(1))
            assertThat(coupon.stock).isEqualTo(beforeStock - 1)
            assertThat(coupon.publishedCoupons.size).isEqualTo(1)
        }

        @Test
        fun `배포기간이 아닐경우 CouponException이 발생한다`() {
            val coupon = CouponFixture(
                publishFrom = LocalDateTime.now().minusDays(3), publishTo = LocalDateTime.now().minusDays(1)
            )
            assertThatThrownBy {
                coupon.publish(
                    UserId(1), LocalDateTime.now()
                )
            }.isInstanceOf(CouponException.CouponNotPublishing::class.java)
        }

        @Test
        fun `수량이 없을 경우 CouponException이 발생한다`() {
            val coupon = CouponFixture(
                stock = 0
            )
            assertThatThrownBy {
                coupon.publish(
                    UserId(1), LocalDateTime.now()
                )
            }.isInstanceOf(CouponException.CouponStockUnavailable::class.java)
        }
    }
}