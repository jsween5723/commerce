package kr.hhplus.be.server.api.point

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.api.Authentication

@Tag(name = "point", description = "포인트 관련 API")
interface PointSpec {
    @Operation(
        summary = "포인트 충전 API",
        description = "인증된 사용자의 포인트를 충전합니다.",
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200", description = "충전 성공 후 결과를 반환합니다."
        ), ApiResponse(responseCode = "401", description = "유효하지 않은 사용자입니다."), ApiResponse(
            responseCode = "400", description = "충전량은 음수가 될수 없습니다."
        ), ApiResponse(responseCode = "400", description = "충전 결과가 최대치를 초과했습니다.")]
    )
    fun charge(authentication: Authentication, request: ChargePointRequest): ChargePointResponse
}

data class ChargePointRequest(@Schema(description = "충전할 양", required = true) val amount: Int)
data class ChargePointResponse(
    @Schema(description = "충전된 user id") val userId: Long,
    @Schema(description = "충전된 후의 포인트") val point: Int
)