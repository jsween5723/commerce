package kr.hhplus.be.server.domain.order.coupon

import kr.hhplus.be.server.domain.coupon.CouponException
import java.math.BigDecimal


sealed interface DiscountPolicy {
    fun discount(target: BigDecimal): BigDecimal
    enum class Type {
        PERCENT, FIXED;

        companion object {
            fun fromString(name: String) = when (name) {
                "PERCENT" -> PERCENT
                "FIXED" -> FIXED
                else -> throw IllegalArgumentException("Unknown discount policy type '$name'")
            }
        }
    }

    companion object {
        fun create(type: Type, amount: BigDecimal): DiscountPolicy {
            return when (type) {
                Type.FIXED -> FixedAmountDiscount(amount)
                Type.PERCENT -> PercentDiscount(amount)
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