package kr.hhplus.be.server.domain.product

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun findAll(): List<Product> = productRepository.findAll()
    fun findRankedBy(query: ProductQuery.Ranked) = productRepository.findRankedBy(query)
    fun insertManyRankedProducts(products: List<RankedProduct>) = productRepository.insertManyRankedProducts(products)

    @Transactional
    fun release(command: ProductCommand.Release): List<Product.ReleaseInfo> {
        val (targets) = command
        val targetProductIds = targets.map { it.productId }
//    내부에서 해당 요소가 모두 존재하는지 검증하여 ProductException을 발생시킵니다.
        if (!productRepository.containsIds(targetProductIds)) throw ProductException.InvalidProductId()
        val products = productRepository.findByIds(targetProductIds).associateBy { it.id }
        // repository레벨에서 검증되기 때문에 !!를 사용합니다.
        return targets.map {
            products[it.productId]!!.release(it.quantity)
        }
    }
}