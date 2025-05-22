package kr.hhplus.be.server.interfaces.support.resolver

import kr.hhplus.be.server.domain.auth.AuthException
import kr.hhplus.be.server.domain.auth.Authentication
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthenticationResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == Authentication::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val authorizationHeader =
            webRequest.getHeader(HttpHeaders.AUTHORIZATION) ?: throw AuthException.InvalidAuthenticationException()
        return Authentication(authorizationHeader.toLong())
    }

}