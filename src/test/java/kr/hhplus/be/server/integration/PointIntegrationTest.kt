package kr.hhplus.be.server.integration

import kr.hhplus.be.server.interfaces.api.point.포인트를_조회한다
import kr.hhplus.be.server.interfaces.api.point.포인트를_충전한다
import kr.hhplus.be.server.support.IntegrationTestSupport
import kr.hhplus.be.server.support.concurrentlyRun
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.concurrent.CompletableFuture

class PointIntegrationTest : IntegrationTestSupport() {
    @Test
    fun `충전을 동시에 2번 실행시 따닥방지`() {
        val userId = idGenerator.userId()
        val useAmount = BigDecimal(200.00)
        //when
        val futures = concurrentlyRun(arrayOf({ 포인트를_충전한다(userId, useAmount) }), count = 2)
        CompletableFuture.allOf(*futures.toTypedArray()).get()
        val point = 포인트를_조회한다(userId).data?.point
        assertThat(point).isEqualTo(useAmount.multiply(2.toBigDecimal()))
    }
}