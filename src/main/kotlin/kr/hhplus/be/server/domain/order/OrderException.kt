package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.CustomException
import kr.hhplus.be.server.domain.ErrorCode

sealed class OrderException(errorCode: OrderErrorCode, throwable: Exception) :
    CustomException(errorCode, throwable) {
    class ReciptIsEmpty :
        OrderException(OrderErrorCode.RECEIPT_IS_EMPTY, IllegalArgumentException())

    class OrderItemIsGreaterThanZero :
        OrderException(OrderErrorCode.ORDER_ITEM_IS_GREATER_THAN_ZERO, IllegalArgumentException())

    class OrderItemInfoValidateFail :
        OrderException(OrderErrorCode.ORDER_ITEM_INFO_VALIDATE_FAIL, IllegalArgumentException())

    class OrderNotFound : OrderException(OrderErrorCode.ORDER_NOT_FOUND, IllegalArgumentException())
    class AleadyCancelled : OrderException(OrderErrorCode.ALREADY_CANCELLED, IllegalArgumentException())
    class AleadyPaid : OrderException(OrderErrorCode.ALREADY_PAID, IllegalArgumentException())
    class PayOnlyPending : OrderException(OrderErrorCode.PAY_ONLY_PENDING, IllegalArgumentException())
    class CancelOnlyNotReleased : OrderException(OrderErrorCode.CANCEL_ONLY_NOT_RELEASED, IllegalArgumentException())
    class FromIsAfterTo : OrderException(OrderErrorCode.FROM_IS_AFTER_TO, IllegalArgumentException())
    class ExpiredCoupons : OrderException(OrderErrorCode.EXPIRED_COUPONS, IllegalArgumentException())
}


enum class OrderErrorCode(override val message: String) : ErrorCode {
    RECEIPT_IS_EMPTY(
        "주문 요소가 비어있습니다."
    ),
    ORDER_ITEM_INFO_VALIDATE_FAIL("주문요소의 정보가 product와 일치하지 않습니다."), ORDER_ITEM_IS_GREATER_THAN_ZERO(
        "주문 요소의 수량이 1개 이상이어야 합니다."
    ),
    ORDER_NOT_FOUND("존재하지 않는 주문정보입니다."),
    CANCEL_ONLY_NOT_RELEASED("주문 취소는 출고되지 않은 상태에서만 가능합니다."),
    ALREADY_CANCELLED("이미 취소된 주문입니다."),
    PAY_ONLY_PENDING("결제는 준비중 상태에서만 가능합니다."),
    ALREADY_PAID("이미 결제완료된 주문입니다."),
    FROM_IS_AFTER_TO("from이 to보다 늦은 시간입니다."),
    EXPIRED_COUPONS("만료된 쿠폰입니다.")
}