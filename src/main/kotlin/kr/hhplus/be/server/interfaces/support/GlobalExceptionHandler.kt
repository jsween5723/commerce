package kr.hhplus.be.server.interfaces.support

import kr.hhplus.be.server.domain.CustomException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.security.cert.CertificateException
import javax.security.auth.login.AccountNotFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handle(exception: CustomException): ResponseEntity<Response<Void>> {
        val status = when (exception.throwable) {
            is AccountNotFoundException -> HttpStatus.UNAUTHORIZED
            is CertificateException -> HttpStatus.FORBIDDEN
            is IllegalArgumentException -> HttpStatus.BAD_REQUEST
            is IllegalStateException -> HttpStatus.CONFLICT
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity.status(status).body(Response.error(exception))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handle(exception: IllegalStateException): ResponseEntity<Response<Void>> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.error(exception))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handle(exception: IllegalArgumentException): ResponseEntity<Response<Void>> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.error(exception))
    }
}