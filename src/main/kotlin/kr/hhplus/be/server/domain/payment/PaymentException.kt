package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.CustomException
import kr.hhplus.be.server.domain.ErrorCode

sealed class PaymentException(errorCode: PaymentErrorCode, throwable: Exception) :
    CustomException(errorCode, throwable) {
    class AlreadyCancelled : PaymentException(
        PaymentErrorCode.ALREADY_CANCELLED, IllegalArgumentException()
    )
// 주문으로만 생성할 수 있으며 주문 생성시 검증되기 때문에 발생할 수 없다.
//    class AmountGreaterThanZero :
//        PaymentException(PaymentErrorCode.AMOUNT_GREATER_THAN_ZERO, IllegalArgumentException())

    class PayOnlyPendingStatus : PaymentException(PaymentErrorCode.PAY_ONLY_PENDING_STATUS, IllegalArgumentException())
}


enum class PaymentErrorCode(override val message: String) : ErrorCode {
    ALREADY_CANCELLED("이미 취소된 결제입니다."), PAY_ONLY_PENDING_STATUS("결제 완료는 결제 대기 상태에서만 가능합니다."), PAYMENT_NOT_FOUND(
        "존재하지 않는 결제정보입니다."
    )
}