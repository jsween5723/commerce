package kr.hhplus.be.server.domain.product

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(private val productRepository: ProductRepository) {
    fun findAll(): List<Product> = productRepository.findAll()
    fun findRankedBy(query: ProductQuery.Ranked) = productRepository.findRankedBy(query)

    @Transactional
    fun release(command: ProductCommand.Release): List<Product.ReleaseInfo> {
        val (targets) = command
        val targetProductIds = targets.map { it.productId }
        val products = productRepository.findByIdsForReleaseOrRestock(targetProductIds).associateBy { it.id }
        return targets.map {
            products[it.productId]?.release(it.quantity) ?: throw ProductException.InvalidProductId()
        }
    }

    @Transactional
    fun restock(command: ProductCommand.Restock) {
        val (targets) = command
        val targetProductIds = targets.map { it.productId }
        val products = productRepository.findByIdsForReleaseOrRestock(targetProductIds).associateBy { it.id }
        targets.forEach {
            products[it.productId]?.restock(it.quantity) ?: throw ProductException.InvalidProductId()
        }
    }

    @Transactional
    fun rank(rankedProduct: RankedProduct) {
        productRepository.insertRankedProduct(rankedProduct)
    }
}