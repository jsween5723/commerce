package kr.hhplus.be.server.domain.auth

import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class AuthenticationTest {
    @Test
    fun `생성할 수 있다`() {
        assertThatCode { Authentication(1) }.doesNotThrowAnyException()
    }

    @Test
    fun `생성시 userId가 1보다 작으면 AuthException이 발생한다`() {
        assertThatThrownBy { Authentication(0) }.isInstanceOf(AuthException.InvalidAuthenticationException::class.java)
    }

    @Test
    fun `super user면 FORBIDDEN이 발생하지 않는다`() {
        val authentication = Authentication(1L, true)
        val userId = UserId(2L)
        assertThatCode { authentication.authorize(userId) }.doesNotThrowAnyException()
    }

    @Test
    fun `userId와 일치하지 않으면 FORBIDDEN이 발생한다`() {
        val authentication = Authentication(1L)
        val userId = UserId(2L)
        assertThatThrownBy { authentication.authorize(userId) }.isInstanceOf(AuthException.ForbiddenException::class.java)
    }

    @Test
    fun `userId와 일치하면 FORBIDDEN이 발생하지 않는다`() {
        val authentication = Authentication(1L)
        val userId = UserId(1L)
        assertThatCode { authentication.authorize(userId) }.doesNotThrowAnyException()
    }
}