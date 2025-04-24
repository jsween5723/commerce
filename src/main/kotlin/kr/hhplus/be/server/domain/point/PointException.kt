package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.CustomException
import kr.hhplus.be.server.domain.ErrorCode

sealed class PointException private constructor(
    errorCode: PointErrorCode, throwable: RuntimeException
) : CustomException(
    errorCode, throwable = throwable
) {

    class MinusAmountCantApply :
        PointException(PointErrorCode.MINUS_AMOUNT_CANT_APPLY, IllegalArgumentException())

    class AmountOverPoint :
        PointException(PointErrorCode.AMOUNT_OVER_POINT, IllegalArgumentException())

    class OverMaxPoint :
        PointException(PointErrorCode.OVER_MAX_POINT, IllegalArgumentException())

    class UnderZeroPoint :
        PointException(PointErrorCode.UNDER_ZERO_POINT, IllegalArgumentException())

    class PleaseTryAgain : PointException(PointErrorCode.PLEASE_TRY_AGAIN, IllegalStateException())
    class InvalidUserId : PointException(PointErrorCode.INVALID_USER_ID, IllegalArgumentException())
}

private enum class PointErrorCode(override val message: String) : ErrorCode {
    UNDER_ZERO_POINT("포인트는 음수가 될 수 없습니다."), OVER_MAX_POINT("포인트 최대치를 초과했습니다."), AMOUNT_OVER_POINT(
        "사용량이 잔액을 초과했습니다"
    ),
    MINUS_AMOUNT_CANT_APPLY(
        "음수로 증감시킬 수 없습니다."
    ),
    PLEASE_TRY_AGAIN("잠시 후 다시 시도해주세요."),
    INVALID_USER_ID("유효하지 않은 대상입니다.");
}