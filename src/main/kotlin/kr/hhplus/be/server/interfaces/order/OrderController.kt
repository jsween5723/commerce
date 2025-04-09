package kr.hhplus.be.server.interfaces.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.interfaces.Response
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
class OrderController : OrderSpec {
    @PostMapping
    override fun create(
        authentication: Authentication,
        @RequestBody request: CreateOrderRequest
    ): Response<CreateOrderResponse> {
        return Response.success(CreateOrderResponse(1L))
    }

    @PostMapping("{id}/pay")
    override fun pay(
        authentication: Authentication,
        @PathVariable id: Long
    ): Response<PayOrderResponse> {
        return Response.success(PayOrderResponse(1L))
    }
}