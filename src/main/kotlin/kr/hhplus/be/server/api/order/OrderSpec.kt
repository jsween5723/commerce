package kr.hhplus.be.server.api.order

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.api.Authentication
import kr.hhplus.be.server.api.ErrorResponse
import kr.hhplus.be.server.api.Response
import org.springframework.http.MediaType

@Tag(name = "주문 / 결제", description = "주문과 주문에 대한 결제를 수행하는 데 관련된 API입니다.")
interface OrderSpec {
    @Operation(
        summary = "주문 생성 API",
        description = "주문을 생성하고 쿠폰을 적용한 후 결제대기 > 주문상품 재고 차감 을 수행합니다.",
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "400", description = "1. 상품 재고가 충분하지 않습니다.", content = [Content(
                schema = Schema(implementation = ErrorResponse::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            )]
        ), ApiResponse(
            responseCode = "404",
            description = "1. 존재하지 않는 상품이 포함됐습니다. \n 2. 존재하지 않는 쿠폰이 포함됐습니다.",
            content = [Content(
                schema = Schema(implementation = ErrorResponse::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            )]
        )]
    )
    fun create(
        authentication: Authentication, request: CreateOrderRequest
    ): Response<CreateOrderResponse>

    @Operation(
        summary = "주문 결제 API",
        description = "주문을 조회하고 포인트를 차감 후 결제를 수행합니다.",
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "400", description = "잔액이 부족합니다.", content = [Content(
                schema = Schema(implementation = ErrorResponse::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            )]
        ), ApiResponse(
            responseCode = "404", description = "존재하지 않는 주문입니다.", content = [Content(
                schema = Schema(implementation = ErrorResponse::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            )]
        )]
    )
    fun pay(
        authentication: Authentication
    ): Response<PayOrderResponse>
}

data class CreateOrderRequest(
    val orderItems: List<CreateOrderItem>, val registeredCouponIds: List<Long>
) {
    data class CreateOrderItem(val productId: Long, @Schema(description = "수량") val amount: Int)
}

data class CreateOrderResponse(@Schema(description = "생성된 주문 id") val id: Long)

data class PayOrderResponse(@Schema(description = "완료된 결제 id") val id: Long)