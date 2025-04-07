package kr.hhplus.be.server.api.point

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
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "point", description = "포인트 관련 API")
interface PointSpec {
    @Operation(
        summary = "포인트 충전 API",
        description = "인증된 사용자의 포인트를 충전합니다.",
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "400",
            description = "1. 충전량은 음수가 될수 없습니다. \n 2. 충전 결과가 최대치를 초과했습니다.",
            content = [Content(
                schema = Schema(implementation = ErrorResponse::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            )]
        )]
    )
    fun charge(
        authentication: Authentication, @RequestBody request: ChargePointRequest
    ): Response<ChargePointResponse>

    @Operation(
        summary = "내 포인트 조회 API",
        description = "인증된 사용자의 포인트를 조회합니다.",
    )
    fun getMyPoint(
        authentication: Authentication
    ): Response<MyPointResponse>
}

data class ChargePointRequest(@Schema(description = "충전할 양", required = true) val amount: Int = 0)

data class ChargePointResponse(
    @Schema(description = "충전된 user id") val userId: Long,
    @Schema(description = "충전된 후의 포인트") val point: Int
)

data class MyPointResponse(
    @Schema(description = "사용자의 id") val userId: Long,
    @Schema(description = "사용자의 포인트") val point: Int
)