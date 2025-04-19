package kr.hhplus.be.server.interfaces.api.point

import kr.hhplus.be.server.application.point.PointCriteria
import kr.hhplus.be.server.application.point.PointFacade
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.interfaces.api.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/points")
class PointController(private val pointFacade: PointFacade) : PointSpec {
    @PostMapping("charge")
    override fun charge(
        authentication: Authentication, request: PointRequest.Charge
    ): Response<PointResponse.Charge> {
        val charge = pointFacade.charge(PointCriteria.Charge(request.amount, authentication))
        return Response.success(data = PointResponse.Charge(success = charge.success))
    }

    @GetMapping("me")
    override fun getMyPoint(authentication: Authentication): Response<PointResponse.MyPoint> {
        val myPoint = pointFacade.myPoint(authentication)
        return Response.success(data = PointResponse.MyPoint(userId = myPoint.userId, point = myPoint.point))
    }
}