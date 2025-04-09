package kr.hhplus.be.server.domain.product

class ProductCommand {
    data class Release(val targets: List<ProductIdAndQuantity>) {
        init {
            if (targets.isEmpty()) throw ProductException.TargetIsEmpty()
        }

        data class ProductIdAndQuantity(val productId: Long, val quantity: Long) {
            init {
                if (productId < 1) throw ProductException.InvalidProductId()
                if (quantity < 1) throw ProductException.AmountMustGreaterThanZero()
            }
        }
    }
}
