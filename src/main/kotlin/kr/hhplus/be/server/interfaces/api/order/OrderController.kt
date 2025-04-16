package kr.hhplus.be.server.interfaces.api.order

import kr.hhplus.be.server.application.order.OrderCriteria
import kr.hhplus.be.server.application.order.OrderFacade
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.interfaces.api.Response
import kr.hhplus.be.server.interfaces.api.order.OrderResponse.CreateOrderResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(private val orderFacade: OrderFacade) : OrderSpec {
    @PostMapping
    override fun create(
        authentication: Authentication,
        @RequestBody request: OrderRequest.CreateOrder
    ): Response<CreateOrderResponse> {
        val created = orderFacade.create(
            OrderCriteria.Create(
                orderItems = request.orderItems.map { it.toCriteria() },
                publishedCouponIds = request.registeredCouponIds,
                authentication = authentication
            )
        )
        return Response.success(CreateOrderResponse(created.orderId))
    }

    @PostMapping("{id}/pay")
    override fun pay(
        authentication: Authentication,
        @PathVariable id: Long
    ): Response<OrderResponse.PayOrderResponse> {
        val paid = orderFacade.pay(
            criteria = OrderCriteria.Pay(
                orderId = id,
                authentication = authentication
            )
        )
        return Response.success(OrderResponse.PayOrderResponse(paid.paymentId))
    }
}