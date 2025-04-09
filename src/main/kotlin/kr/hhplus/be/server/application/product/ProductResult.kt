package kr.hhplus.be.server.application.product

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.RankedProduct

class ProductResult {
    data class GetRankedList(val rankedProducts: List<RankedProduct>)
    data class GetProductList(val products: List<Product>)
}