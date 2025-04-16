package kr.hhplus.be.server.infrastructure.product

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.JoinType
import kr.hhplus.be.server.domain.OrderBy
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductQuery
import kr.hhplus.be.server.domain.product.ProductRepository
import kr.hhplus.be.server.domain.product.RankedProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.LocalDate

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
    private val entityManager: EntityManager,
    private val jdbcTemplate: JdbcTemplate
) : ProductRepository {
    override fun findAll(): List<Product> {
        return productJpaRepository.findAll()
    }

    override fun containsIds(productIds: List<Long>): Boolean {
        return productJpaRepository.countByIdIn(productIds) == productIds.size.toLong()
    }

    override fun findByIds(productIds: List<Long>): List<Product> {
        return productJpaRepository.findByIdIn(productIds)
    }

    override fun findRankedBy(query: ProductQuery.Ranked): List<RankedProduct> {
        val builder = entityManager.criteriaBuilder
        val createdQuery = builder.createQuery(RankedProduct::class.java)
        val root = createdQuery.from(RankedProduct::class.java)
        root.fetch<RankedProduct, Product>("product", JoinType.INNER)
        val predicate = builder.equal(root.get<RankedProduct>("createdDate"), query.day)
        val orders = query.orders.map {
            val path = root.get<RankedProduct>(it.field.name.lowercase())
            if (it.type == OrderBy.Type.DESC) builder.desc(path) else builder.asc(path)
        }.toList()
        return entityManager.createQuery(createdQuery.orderBy(orders).where(predicate)).resultList
    }

    override fun insertManyRankedProducts(products: List<RankedProduct>) {
        val sql =
            "INSERT INTO ranked_products (product_id, total_selling_count, total_income, created_date, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)"
        jdbcTemplate.batchUpdate(sql, object : BatchPreparedStatementSetter {
            override fun setValues(ps: PreparedStatement, i: Int) {
                val product = products[i]
                ps.setLong(1, product.productId)
                ps.setLong(2, product.totalSellingCount)
                ps.setBigDecimal(3, product.totalIncome)
                ps.setDate(4, Date.valueOf(product.createdDate))
                ps.setTimestamp(5, Timestamp.valueOf(product.createdAt))
                ps.setTimestamp(6, Timestamp.valueOf(product.updatedAt))
            }

            override fun getBatchSize(): Int {
                return 100000
            }
        })

    }
}

interface ProductJpaRepository : JpaRepository<Product, Long> {
    fun countByIdIn(ids: List<Long>): Long
    fun findByIdIn(ids: List<Long>): List<Product>

    @Query("select rp from ranked_products rp join fetch rp.product where rp.createdDate = :date  ")
    fun findRankedProductOrderBy(date: LocalDate, query: ProductQuery.Ranked): List<RankedProduct>
}