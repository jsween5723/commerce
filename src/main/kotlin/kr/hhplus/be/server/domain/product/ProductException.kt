package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.CustomException
import kr.hhplus.be.server.domain.ErrorCode

sealed class ProductException(errorCode: ProductErrorCode, throwable: Exception) :
    CustomException(errorCode, throwable) {
    class StockNumberCantBeMinus :
        ProductException(ProductErrorCode.STOCK_NUMBER_CANT_BE_MINUS, IllegalArgumentException())

    class AmountMustGreaterThanZero :
        ProductException(ProductErrorCode.AMOUNT_MUST_GREATER_THAN_ZERO, IllegalArgumentException())

    class PriceCantBeMinus : ProductException(ProductErrorCode.PRICE_CANT_BE_MINUS, IllegalArgumentException())
    class AmountOverStockNumber : ProductException(ProductErrorCode.AMOUNT_OVER_STOCKNUMBER, IllegalArgumentException())
    class InvalidProductId : ProductException(ProductErrorCode.INVALID_PRODUCT_ID, IllegalArgumentException())
    class TargetIsEmpty : ProductException(ProductErrorCode.TARGET_IS_EMPTY, IllegalArgumentException())
}


enum class ProductErrorCode(override val message: String) : ErrorCode {
    STOCK_NUMBER_CANT_BE_MINUS("재고는 음수가 될 수 없습니다."), AMOUNT_MUST_GREATER_THAN_ZERO(
        "증감수량은 0보다 커야합니다."
    ),
    AMOUNT_OVER_STOCKNUMBER("출고량이 재고량을 초과했습니다."),
    INVALID_PRODUCT_ID("유효하지 않은 상품번호입니다"),
    PRICE_CANT_BE_MINUS("가격은 음수가 될 수 없습니다."),
    TARGET_IS_EMPTY("대상이 한 개 이상이어야 합니다.");
}