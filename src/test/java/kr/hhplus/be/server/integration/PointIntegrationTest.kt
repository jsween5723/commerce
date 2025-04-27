package kr.hhplus.be.server.integration

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.point.PointCommand
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.interfaces.api.point.포인트를_조회한다
import kr.hhplus.be.server.interfaces.api.point.포인트를_충전한다
import kr.hhplus.be.server.support.IntegrationTestSupport
import kr.hhplus.be.server.support.concurrentlyRun
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PointIntegrationTest : IntegrationTestSupport() {
    @Autowired
    lateinit var pointService: PointService

    @Test
    fun `충전을 동시에 2번 실행시 1번만 적용`() {
        val userId = idGenerator.userId()
        val useAmount = BigDecimal("200.00")
        //when
        val futures = concurrentlyRun(
            arrayOf(
                { 포인트를_충전한다(userId, useAmount) }, { 포인트를_충전한다(userId, useAmount) })
        )
        val point = 포인트를_조회한다(userId).data!!.point
        assertThat(point.toPlainString()).isEqualTo(useAmount.toPlainString())
    }

    @Test
    fun `차감을 동시에 2번 실행시 1번만 적용`() {
        val userId = idGenerator.userId()
        val useAmount = BigDecimal("200.00")
//        insertTemplate(listOf(UserPointFixture(point = useAmount)))
        //when
        concurrentlyRun(arrayOf({
            pointService.use(
                PointCommand.Use(
                    amount = useAmount,
                    userId = UserId(userId),
                    authentication = Authentication(userId = userId, isSuper = false)
                )
            )
        }, {
            pointService.use(
                PointCommand.Use(
                    amount = useAmount,
                    userId = UserId(userId),
                    authentication = Authentication(userId = userId, isSuper = false)
                )
            )
        }))
        val point = 포인트를_조회한다(userId).data!!.point
        assertThat(point.toPlainString()).isEqualTo(BigDecimal.ZERO.toPlainString())
    }
}