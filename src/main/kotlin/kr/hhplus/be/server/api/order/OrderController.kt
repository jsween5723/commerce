package kr.hhplus.be.server.api.order

import kr.hhplus.be.server.api.Authentication
import kr.hhplus.be.server.api.Response
import kr.hhplus.be.server.api.SuccessResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/orders")
class OrderController : OrderSpec {
    @PostMapping
    override fun create(
        authentication: Authentication,
        @RequestBody request: CreateOrderRequest
    ): Response<CreateOrderResponse> {
        return SuccessResponse(CreateOrderResponse(1L))
    }

    @PostMapping("{id}/pay")
    override fun pay(
        authentication: Authentication,
        @PathVariable id: Long
    ): Response<PayOrderResponse> {
        return SuccessResponse(PayOrderResponse(1L))
    }
}