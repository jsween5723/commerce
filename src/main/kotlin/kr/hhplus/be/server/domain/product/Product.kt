package kr.hhplus.be.server.domain.product

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "products")
class Product(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = 0,
    @Column(nullable = false) val name: String,
    @Column(nullable = false) val price: BigDecimal,
    @Column(nullable = false) var stockNumber: Long
) {

    init {
        validate()
    }

    private fun validate() {
        if (price < BigDecimal.ZERO) throw ProductException.PriceCantBeMinus()
        if (stockNumber < 0) throw ProductException.StockNumberCantBeMinus()
    }


    @CreationTimestamp
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    lateinit var updatedAt: LocalDateTime

    fun release(amount: Long): ReleaseVO {
        if (amount < 1) throw ProductException.AmountMustGreaterThanZero()
        if (stockNumber < amount) throw ProductException.AmountOverStockNumber()
        stockNumber = stockNumber.minus(amount)
        return ReleaseVO(
            quantity = amount
        )
    }

    inner class ReleaseVO(
        val quantity: Long
    ) {
        val product: Product = this@Product

        val totalPrice: BigDecimal get() = product.price.multiply(BigDecimal(quantity))
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ReleaseVO) return false

            if (quantity != other.quantity) return false
            if (product != other.product) return false

            return true
        }

        override fun hashCode(): Int {
            var result = quantity.hashCode()
            result = 31 * result + product.hashCode()
            return result
        }

    }
}

