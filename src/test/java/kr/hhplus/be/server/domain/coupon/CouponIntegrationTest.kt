package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.interfaces.api.coupon.사용자에게_쿠폰을_발급한다
import kr.hhplus.be.server.interfaces.api.coupon.사용자의_쿠폰_목록을_조회한다
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

    @Autowired
    lateinit var publishedCouponRepository: JpaRepository<PublishedCoupon, Long>

    @Test
    fun `존재하지 않는 쿠폰을 발급하면 CouponException이 발생한다`() {
        val userId = idGenerator.userId()
        val couponId = 1L
        val result = 사용자에게_쿠폰을_발급한다(userId, couponId)
        assertThat(result.error?.code).contains(CouponErrorCode.COUPON_NOT_FOUND.name)
    }

    @Test
    fun `쿠폰을 발급하면 내 쿠폰 목록을 조회할 때 확인할 수 있다`() {
        insertTemplate(listOf(CouponFixture(stock = 20L)))
        val userId = idGenerator.userId()
        val couponId = 1L
        val 발급된_쿠폰 = 사용자에게_쿠폰을_발급한다(userId, couponId)
        val 대상_쿠폰 =
            사용자의_쿠폰_목록을_조회한다(userId).data!!.coupons[0]
        assertThat(대상_쿠폰).extracting({ it.coupon.id }, { it.userId })
            .contains(couponId, userId)
    }

    @Test
    fun `동시에 여러 쿠폰을 발급하면 개수가 정상적으로 적용된다`() {
        insertTemplate(listOf(CouponFixture(stock = 3L)))
        val userId = idGenerator.userId()
        val couponId = idGenerator.couponId()
    }

    @Test
    fun `동시에 사용자에게 발급된 쿠폰 사용을 시도하면 하나만 적용된다`() {
        insertTemplate(listOf(CouponFixture(stock = 3L)))
        val userId = idGenerator.userId()
        val publishedCouponId = 사용자에게_쿠폰을_발급한다(userId = userId, couponId = 1).data!!.id
        val exceptions = mutableListOf<RuntimeException>()
        concurrentlyRun(
            {
                try {
                    couponService.selectPublishedCoupons(
                        CouponCommand.Select(
                            Authentication(userId = userId, isSuper = false),
                            listOf(publishedCouponId),
                        )
                    )
                } catch (e: RuntimeException) {
                    exceptions.add(e)
                }

            },
            {
                try {
                    couponService.selectPublishedCoupons(
                        CouponCommand.Select(
                            Authentication(userId = userId, isSuper = false),
                            listOf(publishedCouponId)
                        )
                    )
                } catch (e: RuntimeException) {
                    exceptions.add(e)
                }
            },
        )
        val publishedCoupon = publishedCouponRepository.findById(publishedCouponId)
        assertThat(exceptions.size).isEqualTo(1)
    }

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
        concurrentlyRun(*IntRange(1, publishNumber).map {
            { couponService.publish(command) }
        }.toTypedArray())
        val coupon = couponRepository.findById(couponId)
        assertThat(coupon.get().stock).isEqualTo(previousStock - publishNumber)
    }


}