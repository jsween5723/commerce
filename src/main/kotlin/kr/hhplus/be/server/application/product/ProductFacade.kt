package kr.hhplus.be.server.application.product

import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderQuery
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.product.ProductQuery
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.RankedProduct
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class ProductFacade(private val productService: ProductService, private val orderService: OrderService) {
    fun findRankedProducts(query: ProductQuery.Ranked): ProductResult.GetRankedList =
        ProductResult.GetRankedList(productService.findRankedBy(query))

    fun findAllProducts(): ProductResult.GetProductList = ProductResult.GetProductList(productService.findAll())

    @Transactional
    fun createRankedProductByDate(to: LocalDate, day: Long) {
        val from = to.minusDays(day)
        val orders = orderService.findByForStatistics(OrderQuery.ForStatistics(Order.Status.RELEASED, from, to))
        val rankedProducts =
            // List<List<OrderItem>>
            orders.map { it.receipt.items }
                // List<OrderItem>
                .flatten()
                // Map<Long(productId), List<OrderItem>>
                .groupBy { it.productId }
                // List<RankedProduct>
                .map { (productId, products) ->
                    RankedProduct(
                        productId = productId,
                        totalSellingCount = products.sumOf { it.quantity },
                        totalIncome = products.sumOf { it.totalPrice },
                        createdDate = to
                    )
                }
        productService.insertManyRankedProducts(rankedProducts)
    }
}