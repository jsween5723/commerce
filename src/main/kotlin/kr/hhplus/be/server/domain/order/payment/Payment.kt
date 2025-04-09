package kr.hhplus.be.server.domain.order.payment

import jakarta.persistence.*
import kr.hhplus.be.server.domain.auth.AuthException
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.Order
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "payments")
class Payment protected constructor(
    @Column(nullable = false) val amount: BigDecimal, @Column(nullable = false) val userId: Long
) {
    @Id
    @GeneratedValue
    val id: Long = 0L

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: Status = Status.PENDING

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    fun cancel(authentication: Authentication): PaymentInfo.Cancel {
        authorize(authentication)
        if (status == Status.CANCELLED) throw PaymentException.AlreadyCancelled()
        val amount = if (status == Status.PENDING) BigDecimal.ZERO else amount
        status = Status.CANCELLED
        return PaymentInfo.Cancel(amount)
    }

    fun pay(authentication: Authentication): PaymentInfo.Pay {
        authorize(authentication)
        if (status != Status.PENDING) throw PaymentException.PayOnlyPendingStatus()
        status = Status.PAID
        return PaymentInfo.Pay(amount)
    }

    /**
     * 현재 요구사항에선 조건이 false인 상황이 발생하지 않지만 유지보수상의 방어코드입니다.
     */
    private fun authorize(authentication: Authentication) {
        if (authentication.userId != userId) throw AuthException.ForbiddenException()
    }

    companion object {
        fun from(order: Order) = Payment(amount = order.totalPrice, userId = order.userId)
    }

    enum class Status {
        PENDING, PAID, CANCELLED
    }
}