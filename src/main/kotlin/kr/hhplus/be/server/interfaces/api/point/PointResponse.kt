package kr.hhplus.be.server.interfaces.api.point

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

class PointResponse {
    data class Charge(
        @Schema(description = "충전 성공 여부") val success: Boolean,
    )

    data class MyPoint(
        @Schema(description = "사용자의 id") val userId: Long = 0,
        @Schema(description = "사용자의 포인트") val point: BigDecimal = BigDecimal.ZERO
    )
}