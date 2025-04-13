package kr.hhplus.be.server.domain.order.coupon

import kr.hhplus.be.server.domain.CustomException
import kr.hhplus.be.server.domain.ErrorCode

sealed class CouponException(errorCode: CouponErrorCode, throwable: Exception) : CustomException(errorCode, throwable) {
    class DiscountAmountGreaterThanZero :
        CouponException(CouponErrorCode.DISCOUNT_AMOUNT_GREATER_THAN_ZERO, IllegalStateException())

    class PercentSmallerThan100 : CouponException(CouponErrorCode.PERCENT_SMALLER_THAN_100, IllegalStateException())
}


enum class CouponErrorCode(override val message: String) : ErrorCode {
    DISCOUNT_AMOUNT_GREATER_THAN_ZERO("할인양은 0보다 커야합니다."), PERCENT_SMALLER_THAN_100(
        "비율할인은 100보다 작아야합니다."
    ),
}