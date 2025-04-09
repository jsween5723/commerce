package kr.hhplus.be.server.domain.auth

import io.swagger.v3.oas.annotations.Hidden

@Hidden
data class Authentication(val userId: Long) {
    init {
        validate()
    }

    private fun validate() {
        if (userId <= 0L) throw AuthException.InvalidAuthenticationException()
    }
}