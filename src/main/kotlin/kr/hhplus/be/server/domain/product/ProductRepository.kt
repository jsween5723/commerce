package kr.hhplus.be.server.domain.product

interface ProductRepository {
    fun findAll(): List<Product>

    /*
        내부에서 해당 요소가 모두 존재하는지 검증하여 ProductException을 발생시킵니다.
 */
    fun containsIds(productIds: List<Long>): Boolean
    fun findByIdsForReleaseOrRestock(productIds: List<Long>): List<Product>
    fun findRankedBy(query: ProductQuery.Ranked): List<RankedProduct>
    fun insertRankedProduct(product: RankedProduct)
}