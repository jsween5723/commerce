package kr.hhplus.be.server.domain.coupon


sealed interface DiscountPolicy {
    enum class Type {
        PERCENT, FIXED
    }
}