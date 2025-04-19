package kr.hhplus.be.server.domain.product

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(name = "products")
class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
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

    fun release(amount: Long): ReleaseInfo {
        if (amount < 1) throw ProductException.AmountMustGreaterThanZero()
        if (stockNumber < amount) throw ProductException.AmountOverStockNumber()
        stockNumber = stockNumber.minus(amount)
        return ReleaseInfo(
            quantity = amount
        )
    }

    fun restock(amount: Long) {
        if (amount < 1) throw ProductException.AmountMustGreaterThanZero()
        stockNumber = stockNumber.plus(amount)
    }

    inner class ReleaseInfo(
        val quantity: Long
    ) {
        val product: Product = this@Product

        val totalPrice: BigDecimal get() = product.price.multiply(BigDecimal(quantity))
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ReleaseInfo) return false

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

