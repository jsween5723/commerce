package kr.hhplus.be.server.domain.payment

import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository {
    fun findById(id: Long): Payment?
}