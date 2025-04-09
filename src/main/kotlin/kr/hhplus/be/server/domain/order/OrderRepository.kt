package kr.hhplus.be.server.domain.order

import org.springframework.stereotype.Repository

@Repository
interface OrderRepository {
    fun findById(id: Long): Order?
    fun findByForStatistics(query: OrderQuery.ForStatistics): List<Order>
    fun create(createOrder: CreateOrder): Order
}