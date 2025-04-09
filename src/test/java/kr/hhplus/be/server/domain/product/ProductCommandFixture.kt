package kr.hhplus.be.server.domain.product

class ProductCommandFixture {
    class ProductIdAndQuantity {
        companion object {
            fun create(
                productId: Long = 1L,
                quantity: Long = 2L
            ): ProductCommand.Release.ProductIdAndQuantity {
                return ProductCommand.Release.ProductIdAndQuantity(productId, quantity)
            }
        }
    }
}