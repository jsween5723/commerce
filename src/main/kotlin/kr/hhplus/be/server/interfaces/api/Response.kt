package kr.hhplus.be.server.interfaces.api

import com.fasterxml.jackson.annotation.JsonInclude
import kr.hhplus.be.server.domain.CustomException

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Response<T>(val success: Boolean, val data: T?, val error: ErrorDTO?) {
    companion object {
        fun <T> success(data: T): Response<T> {
            return Response(true, data, null)
        }

        fun <T> error(exception: CustomException): Response<T> {
            return Response(
                false, null, ErrorDTO(code = exception.code, message = exception.localizedMessage)
            )
        }
    }
}

data class ErrorDTO(val code: String, val message: String)