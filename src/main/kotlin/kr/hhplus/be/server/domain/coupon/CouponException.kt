package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.CustomException
import kr.hhplus.be.server.domain.ErrorCode

sealed class CouponException(errorCode: CouponErrorCode, throwable: Exception) : CustomException(errorCode, throwable) {
    class CouponNotFound : CouponException(CouponErrorCode.COUPON_NOT_FOUND, IllegalStateException())
    class CouponStockUnavailable : CouponException(CouponErrorCode.COUPON_STOCK_UNAVAILABLE, IllegalStateException())
    class CouponAlreadyUsed : CouponException(CouponErrorCode.COUPON_ALREADY_USED, IllegalArgumentException())
    class CouponNotPublishing : CouponException(CouponErrorCode.COUPON_NOT_PUBLISHING, IllegalArgumentException())
    class CouponExpired : CouponException(CouponErrorCode.COUPON_EXPIRED, IllegalStateException())
    class DiscountAmountGreaterThanZero :
        CouponException(CouponErrorCode.DISCOUNT_AMOUNT_GREATER_THAN_ZERO, IllegalStateException())

    class PercentSmallerThan100 : CouponException(CouponErrorCode.PERCENT_SMALLER_THAN_100, IllegalStateException())
}


enum class CouponErrorCode(override val message: String) : ErrorCode {
    COUPON_NOT_FOUND("존재하지 않는 쿠폰입니다."), COUPON_STOCK_UNAVAILABLE("발급 대상 쿠폰의 수량이 부족합니다."), COUPON_ALREADY_USED("이미 사용된 쿠폰입니다."),
    COUPON_NOT_PUBLISHING("쿠폰이 배포중이지 않습니다."), COUPON_EXPIRED("쿠폰이 만료되었습니다."), DISCOUNT_AMOUNT_GREATER_THAN_ZERO("할인양은 0보다 커야합니다."), PERCENT_SMALLER_THAN_100(
        "비율할인은 100보다 작아야합니다."
    ),
    PUBLISH_TIME_ERROR("배포기간이 잘못 설정되었습니다.")
}