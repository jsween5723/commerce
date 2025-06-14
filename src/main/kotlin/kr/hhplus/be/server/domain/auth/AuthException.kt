package kr.hhplus.be.server.domain.auth

import kr.hhplus.be.server.domain.CustomException
import kr.hhplus.be.server.domain.ErrorCode
import java.security.cert.CertificateException
import javax.security.auth.login.AccountNotFoundException

sealed class AuthException(
    errorCode: AuthErrorCode, throwable: Exception
) : CustomException(errorCode, throwable) {

    class InvalidAuthenticationException :
        AuthException(AuthErrorCode.INVALID_AUTHENTICATION, AccountNotFoundException())

    class ForbiddenException : AuthException(AuthErrorCode.INVALID_AUTHENTICATION, CertificateException())

}

private enum class AuthErrorCode(override val message: String) : ErrorCode {
    INVALID_AUTHENTICATION("유효하지 않은 인증정보입니다."), FORBIDDEN("권한이 부족합니다.")
}