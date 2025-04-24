package kr.hhplus.be.server.interfaces.api.order

import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import kr.hhplus.be.server.interfaces.api.Response
import org.springframework.http.MediaType

fun 주문한다(
    userId: Long,
    myCouponIds: List<Long>,
    productIdAndQuantities: List<Pair<Long, Long>>
): Response<OrderResponse.CreateOrderResponse> {
    val orderItems = productIdAndQuantities.map { (productId, quantity) ->
        OrderRequest.CreateOrder.CreateOrderItem(
            productId,
            quantity
        )
    }
    val command = OrderRequest.CreateOrder(
        orderItems = orderItems, registeredCouponIds = myCouponIds

    )
    return RestAssured.given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(command).header("Authorization", userId)
        .post("/api/v1/orders")
        .then().log().all()
        .extract().body()
        .`as`(object : TypeRef<Response<OrderResponse.CreateOrderResponse>>() {})
}

fun 주문을_결제한다(
    userId: Long,
    orderId: Long
): Response<OrderResponse.PayOrderResponse> {
    return RestAssured
        .given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", userId)
        .post("/api/v1/orders/$orderId/pay")
        .then().log().all()
        .extract().body()
        .`as`(object : TypeRef<Response<OrderResponse.PayOrderResponse>>() {})
}