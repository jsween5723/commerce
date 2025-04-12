package kr.hhplus.be.server.domain.product

class ProductCommandFixture {
    class ProductIdAndQuantity {
        companion object {
            fun create(
                productId: Long = 1L,
                quantity: Long = 2L
            ): ProductCommand.ProductIdAndQuantity {
                return ProductCommand.ProductIdAndQuantity(productId, quantity)
            }
        }
    }
}