package kr.hhplus.be.server.domain.point

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
class UserPoint(
    @Id val userId: Long = 0L,
    @Column(nullable = false)
    var point: Long,
) {
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
        if (point < 0) throw PointException.UnderZeroPoint()
    }

    fun charge(amount: Long) {
        if (amount < 0) throw PointException.MinusAmountCantApply()
        if (point + amount > MAX) throw PointException.OverMaxPoint()
        point = point.plus(amount)
    }

    fun use(amount: Long) {
        if (amount < 0) throw PointException.MinusAmountCantApply()
        if (point - amount < 0) throw PointException.AmountOverPoint()
        point = point.minus(amount)
    }

    companion object {
        const val MAX = 100_000L
    }
}
