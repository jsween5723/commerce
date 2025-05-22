package kr.hhplus.be.server.interfaces.api.point

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

class PointRequest {
    data class Charge(@Schema(description = "충전할 양", required = true) val amount: BigDecimal = BigDecimal.ZERO)
}