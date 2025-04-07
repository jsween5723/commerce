package kr.hhplus.be.server.api.point

import kr.hhplus.be.server.api.Authentication
import kr.hhplus.be.server.api.Response
import kr.hhplus.be.server.api.SuccessResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/points")
class PointController : PointSpec {
    @PostMapping("charge")
    override fun charge(
        authentication: Authentication, request: ChargePointRequest
    ): Response<ChargePointResponse> {
        return SuccessResponse(data = ChargePointResponse(userId = 3537, point = 1753))
    }

    @GetMapping("me")
    override fun getMyPoint(authentication: Authentication): Response<MyPointResponse> {
        return SuccessResponse(data = MyPointResponse(userId = 3537, point = 1753))
    }
}