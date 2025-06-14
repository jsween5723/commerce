package kr.hhplus.be.server.interfaces.api.coupon

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.interfaces.support.Response
import org.springframework.http.MediaType

@Tag(name = "쿠폰", description = "선착순 쿠폰 관련 API")
interface CouponSpec {
    @Operation(
        summary = "쿠폰 등록 API",
        description = "인증된 사용자에게 쿠폰을 귀속합니다.",
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "400",
            description = "1. 쿠폰의 잔여량이 부족합니다. \n 2. 발급기한이 만료된 쿠폰입니다.",
            content = [Content(
                schema = Schema(implementation = Response::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            )]
        ), ApiResponse(
            responseCode = "404", description = "존재하지 않는 쿠폰입니다.", content = [Content(
                schema = Schema(implementation = Response::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            )]
        )]
    )
    fun register(
        authentication: Authentication, id: Long
    ): Response<CouponResponse.RegisterCouponResponse>

    @Operation(
        summary = "등록된 쿠폰 목록조회 API",
        description = "인증된 사용자에게 귀속된 쿠폰 목록을 조회합니다.",
    )
    fun getMyRegisteredCoupons(
        authentication: Authentication
    ): Response<CouponResponse.GetMyRegisteredCouponsResponse>
}


