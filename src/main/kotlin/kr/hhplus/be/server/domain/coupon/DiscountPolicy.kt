package kr.hhplus.be.server.domain.coupon

import java.math.BigDecimal


sealed interface DiscountPolicy {
    fun discount(target: BigDecimal): BigDecimal

    companion object {
        fun create(type: Coupon.Type, amount: BigDecimal): DiscountPolicy {
            return when (type) {
                Coupon.Type.FIXED -> FixedAmountDiscount(amount)
                Coupon.Type.PERCENT -> PercentDiscount(amount)
            }
        }
    }
}

class FixedAmountDiscount(val amount: BigDecimal) : DiscountPolicy {
    init {
        if (amount < BigDecimal.ONE) throw CouponException.DiscountAmountGreaterThanZero()
    }

    override fun discount(target: BigDecimal): BigDecimal {
        val result = target.minus(amount)
        return if (result < BigDecimal.ZERO) BigDecimal.ZERO else result
    }
}

class PercentDiscount(val amount: BigDecimal) : DiscountPolicy {
    init {
        if (amount < BigDecimal.ONE) throw CouponException.DiscountAmountGreaterThanZero()
        if (amount > BigDecimal.valueOf(100)) throw CouponException.PercentSmallerThan100()
    }

    override fun discount(target: BigDecimal): BigDecimal {
        return target.divide(BigDecimal.valueOf(100)) * (BigDecimal.valueOf(100) - amount)
    }
}