package kr.hhplus.be.server.domain.point

import jakarta.persistence.*
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

    fun charge(amount: BigDecimal) {
        if (amount < BigDecimal.ZERO) throw PointException.MinusAmountCantApply()
        if (point + amount > MAX) throw PointException.OverMaxPoint()
        point = point.plus(amount)
    }

    fun use(amount: BigDecimal) {
        if (amount < BigDecimal.ZERO) throw PointException.MinusAmountCantApply()
        if (point < amount) throw PointException.AmountOverPoint()
        point = point.minus(amount)
    }

    companion object {
        val MAX = BigDecimal(100_000L)
    }
}
