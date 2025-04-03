package kr.hhplus.be.server.api.order

import kr.hhplus.be.server.api.Authentication
import kr.hhplus.be.server.api.Response
import kr.hhplus.be.server.api.SuccessResponse
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderController : OrderSpec {
    override fun create(
        authentication: Authentication,
        request: CreateOrderRequest
    ): Response<CreateOrderResponse> {
        return SuccessResponse(CreateOrderResponse(1L))
    }

    override fun pay(authentication: Authentication): Response<PayOrderResponse> {
        return SuccessResponse(PayOrderResponse(1L))
    }
}