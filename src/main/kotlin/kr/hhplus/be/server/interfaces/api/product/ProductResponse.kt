package kr.hhplus.be.server.interfaces.api.product

import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.RankedProduct
import java.math.BigDecimal
import java.time.LocalDate

class ProductResponse {
    data class GetProductList(val products: List<ProductDTO>) {
        companion object {
            fun from(result: List<Product>) =
                GetProductList(result.map { ProductDTO.from(it) })
        }

        data class ProductDTO(
            val id: Long,
            val name: String,
            val price: BigDecimal,
            val stock: Long
        ) {
            companion object {
                fun from(product: Product) = ProductDTO(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    stock = product.stockNumber
                )
            }
        }
    }

    data class GetRankedProductList(val products: List<RankedProductDTO>) {
        companion object {
            fun from(result: List<RankedProduct>) =
                GetRankedProductList(result.map { RankedProductDTO.from(it) })
        }

        data class RankedProductDTO(
            val productId: Long,
            val name: String,
            val price: BigDecimal,
            val stockNumber: Long,
            val totalSellingCount: Long,
            val totalIncome: BigDecimal,
            val createdDate: LocalDate
        ) {
            companion object {
                fun from(rankedProduct: RankedProduct) = RankedProductDTO(
                    productId = rankedProduct.id,
                    name = rankedProduct.product.name,
                    price = rankedProduct.product.price,
                    stockNumber = rankedProduct.product.stockNumber,
                    totalSellingCount = rankedProduct.totalSellingCount,
                    totalIncome = rankedProduct.totalIncome,
                    createdDate = rankedProduct.createdDate
                )

            }
        }
    }
}
