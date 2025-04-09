package kr.hhplus.be.server.application.order

import kr.hhplus.be.server.domain.order.OrderCommand
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.point.PointCommand
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.product.ProductService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderFacade(
    private val orderService: OrderService,
    private val productService: ProductService,
    private val pointService: PointService
) {
    @Transactional
    fun create(criteria: OrderCriteria.Create): OrderResult.Create {
//        재고 확인 및 재고 선차감
        val releaseInfo = productService.release(criteria.toProductReleaseCommand())
//        주문 생성
        val order = orderService.create(
            OrderCommand.Create(
                releaseItems = releaseInfo, authentication = criteria.authentication
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
        pointService.use(PointCommand.Use(order.totalPrice, criteria.authentication))
//        TODO: 데이터 플랫폼 전송
        return OrderResult.Pay(orderId = order.id, paymentId = order.payment.id)
    }

    @Transactional
    fun cancel(criteria: OrderCriteria.Cancel): OrderResult.Cancel {
//        주문 조회
        val order = orderService.findById(criteria.orderId)
//        주문 취소 및 결제 취소 후 재고 환수
        val info = order.cancel(criteria.authentication)
//        환급 포인트가 있다면 포인트 환급
        if (info.hasAmount) {
            pointService.charge(
                PointCommand.Charge(
                    amount = order.totalPrice, authentication = criteria.authentication
                )
            )
        }
        return OrderResult.Cancel(orderId = order.id, paymentId = order.payment.id)
    }
}