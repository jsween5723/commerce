package kr.hhplus.be.server.interfaces.order.api

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.OrderCommand
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.interfaces.order.api.OrderResponse.CreateOrderResponse
import kr.hhplus.be.server.interfaces.support.Response
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
class OrderController(private val orderService: OrderService) : OrderSpec {
    @PostMapping
    override fun create(
        authentication: Authentication,
        @RequestBody request: OrderRequest.CreateOrder
    ): Response<CreateOrderResponse> {
        val created = orderService.create(
            OrderCommand.Create(
                orderItems = request.orderItems.map { Pair(it.productId, it.amount) },
                publishedCouponIds = request.registeredCouponIds,
                authentication = authentication
            )
        )
        return Response.success(CreateOrderResponse(created.id))
    }

    @PostMapping("{id}/pay")
    override fun pay(
        authentication: Authentication,
        @PathVariable id: Long
    ): Response<OrderResponse.PayOrderResponse> {
        val paid = orderService.pay(
            id, authentication
        )
        return Response.success(OrderResponse.PayOrderResponse(paid.id))
    }
}