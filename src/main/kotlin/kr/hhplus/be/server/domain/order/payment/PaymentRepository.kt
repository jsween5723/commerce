package kr.hhplus.be.server.domain.order.payment

import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository {
    fun findById(id: Long): Payment?
}