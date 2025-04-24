package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.support.IntegrationTestSupport
import kr.hhplus.be.server.support.concurrentlyRun
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponIntegrationTest : IntegrationTestSupport() {
    @Autowired
    lateinit var couponService: CouponService

    @Autowired
    lateinit var couponRepository: JpaRepository<Coupon, Long>


    @Test
    fun `쿠폰 발급시 모두 발급되어 수만큼 재고가 차감 처리된다`() {
//   given
        val previousStock = 30L
        val couponId = 1L
        val publishNumber = 4
        val userId = idGenerator.userId()
        insertTemplate(listOf(CouponFixture(stock = previousStock)))
        val command = CouponCommand.Publish(
            couponId = couponId,
            authentication = Authentication(userId = userId, isSuper = false)
        )
        concurrentlyRun(IntRange(1, publishNumber).map {
            { couponService.publish(command) }
        }.toTypedArray())
        val coupon = couponRepository.findById(couponId)
        assertThat(coupon.get().stock).isEqualTo(previousStock - publishNumber)
    }
}