package kr.hhplus.be.server.domain.payment

import jakarta.persistence.*
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "payments")
class Payment(
    @Column(nullable = false) val amount: BigDecimal, val userId: UserId
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: Status = Status.PENDING

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    fun cancel(authentication: Authentication): PaymentInfo.Cancel {
        authentication.authorize(userId)
        if (status == Status.CANCELLED) throw PaymentException.AlreadyCancelled()
        val amount = if (status == Status.PENDING) BigDecimal.ZERO else amount
        status = Status.CANCELLED
        return PaymentInfo.Cancel(amount)
    }

    fun complete(authentication: Authentication): PaymentInfo.Pay {
        authentication.authorize(userId)
        if (status != Status.PENDING) throw PaymentException.PayOnlyPendingStatus()
        status = Status.PAID
        return PaymentInfo.Pay(amount)
    }

    enum class Status {
        PENDING, PAID, CANCELLED
    }
}