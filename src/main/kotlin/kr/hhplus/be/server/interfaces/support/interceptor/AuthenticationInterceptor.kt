package kr.hhplus.be.server.interfaces.support.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.hhplus.be.server.domain.auth.Authentication
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.servlet.HandlerInterceptor

class AuthenticationInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val threadLocal = ThreadLocal<Authentication>()
        val id = request.getHeader(AUTHORIZATION)
        if (id.toLongOrNull() != null) {
            threadLocal.set(Authentication(id.toLong()))
        }
        return super.preHandle(request, response, handler)
    }
}