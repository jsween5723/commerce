package kr.hhplus.be.server.domain.payment

import kr.hhplus.be.server.domain.auth.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
) {
    @Transactional
    fun create(amount: BigDecimal, authentication: Authentication): Payment {
        return paymentRepository.create(Payment(amount = amount, userId = authentication.id))
    }

    @Transactional
    fun pay(id: Long, authentication: Authentication) {
        val payment = paymentRepository.findById(id) ?: throw IllegalStateException("${id} 존재하지 않는 결제 ID입니다.")
        payment.complete(authentication = authentication)
    }

    @Transactional
    fun complete(id: Long, authentication: Authentication) {
        val payment = paymentRepository.findById(id) ?: throw IllegalStateException("${id} 존재하지 않는 결제 ID입니다.")
        payment.complete(authentication)
    }
}