package kr.hhplus.be.server.interfaces.api.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.interfaces.api.Response
import kr.hhplus.be.server.interfaces.api.order.OrderResponse.CreateOrderResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
class OrderController : OrderSpec {
    @PostMapping
    override fun create(
        authentication: Authentication,
        @RequestBody request: OrderRequest.CreateOrder
    ): Response<CreateOrderResponse> {
        return Response.success(CreateOrderResponse(1L))
    }

    @PostMapping("{id}/pay")
    override fun pay(
        authentication: Authentication,
        @PathVariable id: Long
    ): Response<OrderResponse.PayOrderResponse> {
        return Response.success(OrderResponse.PayOrderResponse(1L))
    }
}