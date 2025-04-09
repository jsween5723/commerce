package kr.hhplus.be.server.domain.point

import jakarta.persistence.*
import kr.hhplus.be.server.domain.auth.AuthException
import kr.hhplus.be.server.domain.auth.Authentication
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
class UserPoint(
    val userId: Long = 0L,
    @Column(nullable = false)
    var point: BigDecimal = BigDecimal.ZERO,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var updatedAt: LocalDateTime

    init {
        validate()
    }

    private fun validate() {
        if (point < BigDecimal.ZERO) throw PointException.UnderZeroPoint()
    }

    fun charge(amount: BigDecimal, authentication: Authentication) {
        authorize(authentication)
        if (amount < BigDecimal.ZERO) throw PointException.MinusAmountCantApply()
        if (point + amount > MAX) throw PointException.OverMaxPoint()
        point = point.plus(amount)
    }

    fun use(amount: BigDecimal, authentication: Authentication) {
        authorize(authentication)
        if (amount < BigDecimal.ZERO) throw PointException.MinusAmountCantApply()
        if (point < amount) throw PointException.AmountOverPoint()
        point = point.minus(amount)
    }

    private fun authorize(authentication: Authentication) {
        if (!authentication.isSuper && authentication.userId != userId) throw AuthException.ForbiddenException()
    }


    companion object {
        val MAX = BigDecimal(100_000L)
    }
}
