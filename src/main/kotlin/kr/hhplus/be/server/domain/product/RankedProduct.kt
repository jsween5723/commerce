package kr.hhplus.be.server.domain.product

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(name = "ranked_products")
/**
 * 순위가 필드에 없는 이유 : 어차피 총 판매량이랑 판매금액 중 어떤 것을 골라 orderBy할 것이기 때문
 */
class RankedProduct(
    @Column(nullable = false, name = "product_id") val productId: Long,
    @Column(nullable = false) val totalSellingCount: Long,
    @Column(nullable = false) val totalIncome: BigDecimal,
    @Column(nullable = false) val createdDate: LocalDate
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    lateinit var product: Product

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

}