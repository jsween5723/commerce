package kr.hhplus.be.server.integration

import kr.hhplus.be.server.application.coupon.CouponCriteria
import kr.hhplus.be.server.application.coupon.CouponFacade
import kr.hhplus.be.server.application.order.OrderCriteria
import kr.hhplus.be.server.application.order.OrderFacade
import kr.hhplus.be.server.application.point.PointCriteria
import kr.hhplus.be.server.application.point.PointFacade
import kr.hhplus.be.server.application.product.ProductFacade
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.OrderException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderFacadeIntegrationTest : IntegrationTestSupport() {
    @Autowired
    private lateinit var orderFacade: OrderFacade

    @Autowired
    private lateinit var couponFacade: CouponFacade

    @Autowired
    private lateinit var productFacade: ProductFacade

    @Autowired
    private lateinit var pointFacade: PointFacade

    @BeforeAll
    fun beforeAll() {
        insertProducts()
        insertCoupons()
    }

    @Test
    fun `주문 생성 및 결제, 취소 플로우`() {
        val userId = longFixture.userId()

//        포인트 충전
        pointFacade.charge(
            PointCriteria.Charge(
                BigDecimal(10000000L),
                authentication = Authentication(userId = userId)
            )
        )
        val products = productFacade.findAllProducts()
        val orderItems = products.products.slice(0..MAX_COUNT.toInt() - 1).mapIndexed { index, products ->
            OrderCriteria.Create.CreateOrderItem(
                products.id,
                amount = index + 1L
            )
        }
// 쿠폰 발급
        val couponId = longFixture.couponId() * 2 - 1
        val publishedCouponId = couponFacade.publish(
            CouponCriteria.Publish(
                authentication = Authentication(userId),
                couponId = couponId
            )
        ).publishedCouponId

//  주문하기
        val order = orderFacade.create(
            OrderCriteria.Create(
                orderItems = orderItems,
                publishedCouponIds = listOf(publishedCouponId),
                authentication = Authentication(userId = userId)
            )
        )
        orderFacade.cancelByDate(
            OrderCriteria.CancelBy(
                pendingTime = LocalDateTime.now(),
                authentication = Authentication(userId = userId, isSuper = true)
            )
        )
// 주문 결제하기 실패 (취소됨)
        assertThatThrownBy {
            orderFacade.pay(
                OrderCriteria.Pay(
                    orderId = order.orderId,
                    authentication = Authentication(userId = userId)
                )
            )
        }.isInstanceOf(OrderException::class.java)

//        재주문
        val order2 = orderFacade.create(
            OrderCriteria.Create(
                orderItems = orderItems,
                publishedCouponIds = listOf(publishedCouponId),
                authentication = Authentication(userId = userId)
            )
        )
//        결제 성공
        orderFacade.pay(
            OrderCriteria.Pay(
                orderId = order2.orderId,
                authentication = Authentication(userId = userId)
            )
        )
    }
}