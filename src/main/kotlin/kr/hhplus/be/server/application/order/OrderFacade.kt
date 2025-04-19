package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.order.*
import kr.hhplus.be.server.domain.point.PointCommand
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.ProductCommand
import kr.hhplus.be.server.domain.product.ProductService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderFacade(
    private val orderService: OrderService,
    private val productService: ProductService,
    private val pointService: PointService,
    private val dataPlatformSender: DataPlatformSender,
    private val couponService: CouponService,
) {
    @Transactional
    fun create(criteria: OrderCriteria.Create): OrderResult.Create {
//        재고 확인 및 재고 선차감
        val productSnapshots = productService.release(criteria.toProductReleaseCommand())
            .map { OrderMapper.toSelectedProductAndQuantitySnapshot(it) }
        val selectedCoupons = couponService.findPublishedByIds(criteria.publishedCouponIds)
            .map { OrderMapper.toSelectedCouponSnapshot(it) }
//        주문 생성
        val order = orderService.create(
            OrderCommand.Create(
                selectedProducts = productSnapshots,
                publishedCoupons = selectedCoupons,
                authentication = criteria.authentication
            )
        )
//        주문 id 반환
        return OrderResult.Create(orderId = order.id)
    }

    @Transactional
    fun pay(criteria: OrderCriteria.Pay): OrderResult.Pay {
//        주문 조회
        val order = orderService.findById(criteria.orderId)
//        결제 완료 처리
        val info = order.pay(criteria.authentication)
        //        포인트 차감

        pointService.use(PointCommand.Use(order.totalPrice, criteria.authentication.id, criteria.authentication))
        dataPlatformSender.send(order)
        return OrderResult.Pay(orderId = order.id, paymentId = order.payment.id)
    }

    @Transactional
    fun cancelByDate(criteria: OrderCriteria.CancelBy) {
        val (pending, authentication) = criteria
        val orders =
            orderService.findForCancel(OrderQuery.ForCancelSchedule(Order.Status.PENDING, criteria.pendingTime))
        orders.forEach { order ->
            val info = order.cancel(authentication)
            val idAndQuantities = OrderMapper.toProductIdAndQuantities(order.receipt)
            productService.restock(ProductCommand.Restock(idAndQuantities))
        }
    }
}