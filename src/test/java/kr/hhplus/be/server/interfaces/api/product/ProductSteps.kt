package kr.hhplus.be.server.interfaces.api.product

import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import kr.hhplus.be.server.domain.product.ProductQuery
import kr.hhplus.be.server.interfaces.support.Response

fun 상품목록을_조회한다(): Response<ProductResponse.GetProductList> {
    return RestAssured.given().log().all()
        .get("/api/v1/products")
        .then().log().all()
        .extract()
        .body()
        .`as`(object : TypeRef<Response<ProductResponse.GetProductList>>() {})
}

fun 인기_상품목록을_조회한다(query: ProductQuery.Ranked): Response<ProductResponse.GetRankedProductList> {
    return RestAssured.given().log().all()
        .get("/api/v1/products/popular")
        .then().log().all()
        .extract()
        .body()
        .`as`(object : TypeRef<Response<ProductResponse.GetRankedProductList>>() {})
}
