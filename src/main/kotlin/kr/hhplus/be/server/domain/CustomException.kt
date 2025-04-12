package kr.hhplus.be.server.domain

open class CustomException(
    errorCode: ErrorCode,
    val throwable: Exception
) : RuntimeException(errorCode.message) {
    val code = errorCode.name
}
