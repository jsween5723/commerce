package kr.hhplus.be.server.interfaces.api.point

import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import kr.hhplus.be.server.interfaces.support.Response
import kr.hhplus.be.server.interfaces.point.api.PointRequest
import kr.hhplus.be.server.interfaces.point.api.PointResponse
import org.springframework.http.MediaType
import java.math.BigDecimal


fun 포인트를_충전한다(userId: Long, amount: BigDecimal): Response<PointResponse.Charge> {
    val command = PointRequest.Charge(amount)
    return RestAssured.given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(command)
        .header("Authorization", userId)
        .post("/api/v1/points/charge").then().log().all()
        .extract()
        .body()
        .`as`(object : TypeRef<Response<PointResponse.Charge>>() {})
}

fun 포인트를_조회한다(userId: Long): Response<PointResponse.MyPoint> {
    return RestAssured.given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", userId)
        .get("/api/v1/points/me").then().log().all()
        .extract()
        .body()
        .`as`(object : TypeRef<Response<PointResponse.MyPoint>>() {})
}