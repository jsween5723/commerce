package kr.hhplus.be.server.application.product

import kr.hhplus.be.server.domain.product.ProductService
import org.springframework.stereotype.Component

@Component
class ProductFacade(private val productService: ProductService) {
    fun findRankedProducts(): ProductResult.GetRankedList =
        ProductResult.GetRankedList(productService.findAllRanked())

    fun findAllProducts(): ProductResult.GetProductList =
        ProductResult.GetProductList(productService.findAll())
}