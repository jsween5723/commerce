package kr.hhplus.be.server.infrastructure.product

import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.LockModeType
import jakarta.persistence.criteria.JoinType
import kr.hhplus.be.server.domain.OrderBy
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductQuery
import kr.hhplus.be.server.domain.product.ProductRepository
import kr.hhplus.be.server.domain.product.RankedProduct
import org.hibernate.SessionFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
    private val entityManager: EntityManager,
    private val jdbcTemplate: JdbcTemplate,
    private val entityManagerFactory: EntityManagerFactory,
    private val rankedProductRepository: RankedProductRepository
) : ProductRepository {
    override fun findAll(): List<Product> {
        return productJpaRepository.findAll()
    }

    override fun containsIds(productIds: List<Long>): Boolean {
        return productJpaRepository.countByIdIn(productIds) == productIds.size.toLong()
    }

    override fun findByIdsForReleaseOrRestock(productIds: List<Long>): List<Product> {
        return productJpaRepository.findByIdIn(productIds)
    }

    override fun findRankedBy(query: ProductQuery.Ranked): List<RankedProduct> {
        val builder = entityManager.criteriaBuilder
        val createdQuery = builder.createQuery(RankedProduct::class.java)
        val root = createdQuery.from(RankedProduct::class.java)
        root.fetch<RankedProduct, Product>("product", JoinType.INNER)
        val predicate = builder.equal(root.get<RankedProduct>("createdDate"), query.day)
        val orders = query.orders.map {
            val path = root.get<RankedProduct>(it.field.field)
            if (it.type == OrderBy.Type.DESC) builder.desc(path) else builder.asc(path)
        }.toList()
        return entityManager.createQuery(createdQuery.orderBy(orders).where(predicate)).resultList
    }

    override fun insertManyRankedProducts(products: List<RankedProduct>) {
        entityManagerFactory.unwrap(SessionFactory::class.java).withStatelessOptions().openStatelessSession().use {
            val tx = it.beginTransaction()
            try {
                rankedProductRepository.saveAll(products)
                tx.commit()
            } catch (e: Exception) {
                tx.rollback()
            } finally {

            }
        }
    }
}

interface ProductJpaRepository : JpaRepository<Product, Long> {
    fun countByIdIn(ids: List<Long>): Long

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByIdIn(ids: List<Long>): List<Product>

    @Query("select rp from ranked_products rp join fetch rp.product where rp.createdDate = :date  ")
    fun findRankedProductOrderBy(date: LocalDate, query: ProductQuery.Ranked): List<RankedProduct>
}

interface RankedProductRepository : JpaRepository<RankedProduct, Long>