package kr.hhplus.be.server.domain.product

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(name = "ranked_products")
class RankedProduct(
    @ManyToOne @JoinColumn(
        name = "product_id", nullable = false
    ) val product: Product,
    @Column(nullable = false) val totalSellingCount: Long,
    @Column(nullable = false) val totalIncome: BigDecimal,
    @Column(nullable = false) val rank: Int,
    @Column(nullable = false) val createdDate: LocalDate
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

}