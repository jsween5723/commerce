package kr.hhplus.be.server.domain.order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(private val orderRepository: OrderRepository) {
    fun findById(id: Long): Order = orderRepository.findById(id) ?: throw OrderException.OrderNotFound()
    fun findByForStatistics(query: OrderQuery.ForStatistics) = orderRepository.findByForStatistics(
        query
    )

    @Transactional
    fun create(command: OrderCommand.Create): Order {
        val (releaseItems, authentication) = command
        val createOrder = CreateOrder.from(releaseItems, authentication)
        return orderRepository.create(createOrder)
    }
}