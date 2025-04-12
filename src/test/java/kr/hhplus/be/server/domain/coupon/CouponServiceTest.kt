package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.auth.Authentication
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class CouponServiceTest {
    val couponRepository: CouponRepository = mock(CouponRepository::class.java)
    val couponService = CouponService(couponRepository = couponRepository)

    @Nested
    inner class Publish {
        @Test
        fun `쿠폰이 유효하지 않으면 예외를 반환한다`() {
//            given
            `when`(couponRepository.findById(1)).thenReturn(null)
            assertThatThrownBy {
                couponService.publish(CouponCommand.Publish(authentication = Authentication(1), 1))
            }.isInstanceOf(CouponException.CouponNotFound::class.java)
        }

        @Test
        fun `발급할 수 있다`() {
//            given
            `when`(couponRepository.findById(1)).thenReturn(CouponFixture())
            assertThatCode {
                couponService.publish(CouponCommand.Publish(authentication = Authentication(1), 1))
            }.doesNotThrowAnyException()
        }
    }
}