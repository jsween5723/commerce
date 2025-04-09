package kr.hhplus.be.server.interfaces.product

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.interfaces.Response


@Tag(name = "상품", description = "상품 관련 API")
interface ProductSpec {
    @Operation(
        summary = "상품 목록 조회 API",
        description = "모든 상품을 조회합니다.",
    )
    fun getList(): Response<ProductResponse.GetProductList>

    @Operation(
        summary = "인기 상품 목록 조회 API",
        description = "근 3일간 판매량이 많은 순서대로 순위를 매겨 상품을 조회합니다.",
    )
    fun getRankedList(): Response<ProductResponse.GetRankedProductList>
}



