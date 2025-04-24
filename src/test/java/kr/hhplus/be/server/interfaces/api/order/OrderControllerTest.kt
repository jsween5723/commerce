package kr.hhplus.be.server.interfaces.api.order

import kr.hhplus.be.server.domain.point.UserPointFixture
import kr.hhplus.be.server.domain.product.ProductFixture
import kr.hhplus.be.server.support.IntegrationTestSupport
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OrderControllerTest : IntegrationTestSupport() {

    @Test
    fun `1_POST api_v1_orders`() {
        val userId = idGenerator.userId()
        insertTemplate(listOf(UserPointFixture(point = BigDecimal(10000000), userId = userId)))
        주문한다(userId = userId, myCouponIds = listOf(), productIdAndQuantities = listOf(Pair(1, 1)))
    }

    @Test
    fun `2_POST api_v1_orders_{id}_pay`() {
        insertTemplate(
            listOf(ProductFixture())
        )
        val userId = idGenerator.userId()
        insertTemplate(listOf(UserPointFixture(point = BigDecimal(10000000), userId = userId)))
        val orderId = 주문한다(
            userId = userId,
            myCouponIds = listOf(),
            productIdAndQuantities = listOf(Pair(1, 1))
        ).data!!.id
        주문을_결제한다(userId, orderId)
    }
}