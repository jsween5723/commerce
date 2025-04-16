package kr.hhplus.be.server.infrastructure.order

import kr.hhplus.be.server.domain.order.CreateOrder
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderQuery
import kr.hhplus.be.server.domain.order.OrderRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class OrderRepositoryImpl(private val orderJpaRepository: OrderJpaRepository) : OrderRepository {
    override fun findById(id: Long): Order? {
        return orderJpaRepository.findByIdOrNull(id)
    }

    override fun findByForStatistics(query: OrderQuery.ForStatistics): List<Order> {
        val from = LocalDateTime.from(query.from)
        val to = LocalDateTime.from(query.to)
        return orderJpaRepository.findAllByStatusAndUpdatedAtBetween(
            status = query.status,
            updatedAtAfter = from,
            updatedAtBefore = to
        )
    }

    override fun findForCancel(query: OrderQuery.ForCancelSchedule): List<Order> {
        return orderJpaRepository.findByStatusAndUpdatedAtBefore(query.status, query.until)
    }

    override fun create(createOrder: CreateOrder): Order {
        return orderJpaRepository.save(createOrder)
    }
}

interface OrderJpaRepository : JpaRepository<Order, Long> {
    fun findAllByStatusAndUpdatedAtBetween(
        status: Order.Status,
        updatedAtAfter: LocalDateTime,
        updatedAtBefore: LocalDateTime
    ): List<Order>

    fun findByStatusAndUpdatedAtBefore(status: Order.Status, updatedAtBefore: LocalDateTime): List<Order>
}