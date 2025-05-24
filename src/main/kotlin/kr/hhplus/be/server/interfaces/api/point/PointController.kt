package kr.hhplus.be.server.interfaces.api.point

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.point.PointCommand
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.interfaces.support.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/points")
class PointController(private val pointService: PointService) : PointSpec {
    @PostMapping("charge")
    override fun charge(
        authentication: Authentication, request: PointRequest.Charge
    ): Response<PointResponse.Charge> {
        val charge = pointService.charge(
            PointCommand.Charge(
                amount = request.amount,
                userId = authentication.id,
                authentication = authentication
            )
        )
        return Response.success(data = PointResponse.Charge(success = true))
    }

    @GetMapping("me")
    override fun getMyPoint(authentication: Authentication): Response<PointResponse.MyPoint> {
        val myPoint = pointService.findByUserId(authentication.id)
        return Response.success(data = PointResponse.MyPoint(userId = myPoint.userId, point = myPoint.point))
    }
}