package kr.hhplus.be.server.domain.order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrderService(private val orderRepository: OrderRepository) {
    fun findById(id: Long): Order = orderRepository.findById(id) ?: throw OrderException.OrderNotFound()
    fun findByForStatistics(query: OrderQuery.ForStatistics) = orderRepository.findByForStatistics(
        query
    )

    fun findForCancel(query: OrderQuery.ForCancelSchedule) = orderRepository.findForCancel(query)

    @Transactional
    fun create(command: OrderCommand.Create): Order {
        val (releaseItems, publishedCoupons, authentication) = command
        val createOrder = CreateOrder.from(releaseItems, authentication, LocalDateTime.now(), publishedCoupons)
        return orderRepository.create(createOrder)
    }
}