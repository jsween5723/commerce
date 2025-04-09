package kr.hhplus.be.server.domain.order

import org.springframework.stereotype.Repository

@Repository
interface OrderRepository {
    fun findById(id: Long): Order?
    fun create(createOrder: CreateOrder): Order
}