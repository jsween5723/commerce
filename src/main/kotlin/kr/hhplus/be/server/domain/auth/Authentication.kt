package kr.hhplus.be.server.domain.auth

import io.swagger.v3.oas.annotations.Hidden
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Hidden
class Authentication(userId: Long, @Transient val isSuper: Boolean = false) {
    val id: UserId = UserId(userId)

    init {
        validate()
    }

    private fun validate() {
        if (id.userId <= 0L) throw AuthException.InvalidAuthenticationException()
    }

    fun authorize(userId: UserId) {
        if (!isSuper && id.userId != userId.userId) throw AuthException.ForbiddenException()
    }
}

@Embeddable
data class UserId(@Column(nullable = false) val userId: Long)