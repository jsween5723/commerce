package kr.hhplus.be.server.interfaces.api.point

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.interfaces.api.Response
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
        return Response.success(data = ChargePointResponse(userId = 3537, point = 1753))
    }

    @GetMapping("me")
    override fun getMyPoint(authentication: Authentication): Response<MyPointResponse> {
        return Response.success(data = MyPointResponse(userId = 3537, point = 1753))
    }
}