package kr.hhplus.be.server.domain.point

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Version
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "user_points")
class UserPoint(
    userId: Long,
    @Column(nullable = false) var point: BigDecimal = BigDecimal.ZERO,
) {
    @Id
    val userId: Long = userId

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: LocalDateTime

    @Version
    var version: Long = 0

    init {
        validate()
    }

    private fun validate() {
        if (point < BigDecimal.ZERO) throw PointException.UnderZeroPoint()
    }

    fun charge(amount: BigDecimal, authentication: Authentication) {
        authentication.authorize(UserId(userId))
        if (amount < BigDecimal.ZERO) throw PointException.MinusAmountCantApply()
        if (point + amount > MAX) throw PointException.OverMaxPoint()
        point = point.plus(amount)
    }

    fun use(amount: BigDecimal, authentication: Authentication) {
        authentication.authorize(UserId(userId))
        if (amount < BigDecimal.ZERO) throw PointException.MinusAmountCantApply()
        if (point < amount) throw PointException.AmountOverPoint()
        point = point.minus(amount)
    }

    companion object {
        val MAX = BigDecimal(100_000_000L)
    }
}
