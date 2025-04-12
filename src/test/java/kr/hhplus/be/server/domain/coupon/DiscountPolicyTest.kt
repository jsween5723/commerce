package kr.hhplus.be.server.domain.coupon

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DiscountPolicyTest {
    @Nested
    inner class `FixedAmountDiscount` {
        @Test
        fun `생성할 수 있다`() {
            assertThatCode {
                DiscountPolicy.create(DiscountPolicy.Type.FIXED, BigDecimal.valueOf(100))
            }.doesNotThrowAnyException()
        }

        @Test
        fun `할인양이 음수면 CouponException을 발생시킨다`() {
            assertThatThrownBy {
                DiscountPolicy.create(DiscountPolicy.Type.FIXED, BigDecimal.valueOf(-100))
            }.isInstanceOf(CouponException.DiscountAmountGreaterThanZero::class.java)
        }

        @Test
        fun `정량 할인을 적용할 수 있다`() {
            //given
            val before = BigDecimal.valueOf(100)
            val amount = BigDecimal.valueOf(20)
            val policy = DiscountPolicy.create(DiscountPolicy.Type.FIXED, amount)
            assertThat(policy.discount(before)).isEqualTo(before - amount)
        }

        @Test
        fun `정량 할인양이 원본보다 크면 0이 된다`() {
            //given
            val before = BigDecimal.valueOf(100)
            val amount = BigDecimal.valueOf(200)
            val policy = DiscountPolicy.create(DiscountPolicy.Type.FIXED, amount)
            assertThat(policy.discount(before)).isEqualTo(BigDecimal.ZERO)
        }
    }

    @Nested
    inner class `PercentDiscount` {
        @Test
        fun `생성할 수 있다`() {
            assertThatCode {
                DiscountPolicy.create(DiscountPolicy.Type.PERCENT, BigDecimal.valueOf(100))
            }.doesNotThrowAnyException()
        }

        @Test
        fun `할인양이 음수면 CouponException을 발생시킨다`() {
            assertThatThrownBy {
                DiscountPolicy.create(DiscountPolicy.Type.PERCENT, BigDecimal.valueOf(-100))
            }.isInstanceOf(CouponException.DiscountAmountGreaterThanZero::class.java)
        }

        @Test
        fun `할인비율이 100보다 크면 CouponException을 발생시킨다`() {
            assertThatThrownBy {
                DiscountPolicy.create(DiscountPolicy.Type.PERCENT, BigDecimal.valueOf(101))
            }.isInstanceOf(CouponException.PercentSmallerThan100::class.java)
        }

        @Test
        fun `비율 할인을 적용할 수 있다`() {
            //given
            val before = BigDecimal.valueOf(100)
            val amount = BigDecimal.valueOf(20)
            val expected = BigDecimal.valueOf(80)
            val policy = DiscountPolicy.create(DiscountPolicy.Type.PERCENT, amount)
            assertThat(policy.discount(before)).isEqualTo(expected)
        }


    }
}