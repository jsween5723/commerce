package kr.hhplus.be.server.interfaces.api.coupon

import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import kr.hhplus.be.server.interfaces.api.Response
import org.springframework.http.MediaType

fun `사용자에게_쿠폰을_발급한다`(userId: Long, couponId: Long): Response<CouponResponse.RegisterCouponResponse> {
    return RestAssured.given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", userId)
        .`when`()
        .post("/api/v1/coupons/$couponId/register")
        .then().log().all().extract()
        .body().`as`(object : TypeRef<Response<CouponResponse.RegisterCouponResponse>>() {})
}

fun 사용자의_쿠폰_목록을_조회한다(userId: Long): Response<CouponResponse.GetMyRegisteredCouponsResponse> {
    return RestAssured.given().log().all()
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header("Authorization", userId)
        .`when`()
        .get("/api/v1/coupons/me")
        .then().log().all().extract()
        .body().`as`(object : TypeRef<Response<CouponResponse.GetMyRegisteredCouponsResponse>>() {})
}