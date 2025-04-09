package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.payment.PaymentService
import kr.hhplus.be.server.domain.point.PointService
import org.springframework.stereotype.Component

@Component
class OrderFacade(
    private val orderService: OrderService,
    private val pointService: PointService,
    private val paymentService: PaymentService
) {
    fun create() {

    }

    fun pay() {

    }
}