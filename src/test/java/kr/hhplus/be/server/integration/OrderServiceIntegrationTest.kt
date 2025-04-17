package kr.hhplus.be.server.integration

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.product.ProductSnapshotFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@TestInstance(
    TestInstance.Lifecycle.PER_CLASS
)
class OrderServiceIntegrationTest : IntegrationTestSupport() {
    @BeforeAll
    fun beforeAll() {
        insertCoupons()
    }


    @Test
    fun `상품을 조회하고 주문한 뒤 결제할 수 있다_취소되면_상태가_바뀐다`() {
        val userId = longFixture.userId()

//        상품 조회
        val products = 상품목록을_조회한다()
        val productIds = products.filter { it.stockNumber > 0 }.map { it.id }
        val productSnapshots = productIds.mapIndexed { index, productId ->
            ProductSnapshotFixture(
                quantity = index.toLong() + 1,
                productId = productId
            )
        }

//        쿠폰 발급
        val couponId = longFixture.couponId() * 2 + 1
        val 발급된_쿠폰 = 쿠폰을_발급한다(userId, couponId)
        val 대상_쿠폰 = 대상_쿠폰_목록을_사용한다(userId, listOf(발급된_쿠폰.id))[0]

//        주문 생성
        val order = 주문한다(
            userId = userId,
            publishCoupons = listOf(),
            productIdAndQuantities = productSnapshots
        )

//        주문 결제
        order.pay(Authentication(userId))
        assertThat(order.usedCoupons.items.isEmpty()).isTrue()
        assertThat(order.receipt.items.size).isEqualTo(productSnapshots.size)
        assertThat(order.totalPrice).isEqualTo(order.usedCoupons.discount(productSnapshots.sumOf { it.totalPrice }))
        assertThat(order.status).isEqualTo(Order.Status.PAID)
//        주문 취소
        order.cancel(Authentication(userId))
        assertThat(order.status).isEqualTo(Order.Status.CANCELLED)
    }
}