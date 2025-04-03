package kr.hhplus.be.server.api

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
open class Response<T>(val success: Boolean, open val data: T?, open val error: Error?)

data class SuccessResponse<T>(override val data: T) :
    Response<T>(success = true, data = data, error = null)

data class Error(val message: String, val code: String)

enum class ErrorCode(private val message: String) {
    OVER_MAX_POINT("포인트 최대치를 초과했습니다."),
    UNAUTHORIZED("유효하지 않은 사용자입니다.");

    fun toError(): Error {
        return Error(message, name)
    }
}

@Schema(description = "에러 응답")
class ErrorResponse(code: ErrorCode) : Response<Void>(
    success = false, data = null, error = code.toError()
)