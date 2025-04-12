package kr.hhplus.be.server.application.point

import kr.hhplus.be.server.domain.point.PointLockTemplate
import kr.hhplus.be.server.domain.point.PointService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PointFacade(
    private val pointService: PointService, private val lockManager: PointLockTemplate
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)


    @Transactional
    fun charge(criteria: PointCriteria.Charge): PointResult.Charge {
        val (amount, authentication) = criteria
        val result = lockManager.runWithLock(authentication.id.userId) {
            val point = pointService.charge(criteria.toChargeCommand())
        }
        return PointResult.Charge(true)
    }
}