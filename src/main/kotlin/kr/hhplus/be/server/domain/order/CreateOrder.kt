package kr.hhplus.be.server.domain.order

import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.product.Product
import java.math.BigDecimal
import java.util.*


class CreateOrder private constructor(val receipt: CreateReceipt, val userId: Long) {

    companion object {
        fun from(releaseItems: List<Product.ReleaseInfo>, authentication: Authentication) =
            CreateOrder(receipt = CreateReceipt.from(releaseItems), userId = authentication.userId)
    }
}

//생성 경로를 ReleaseVO 목록으로 제한합니다.
//따라서 해당 클래스에서 검증하는 역할은 수행하지 않습니다.
class CreateReceipt private constructor(
    val items: List<CreateOrderItem> = LinkedList()
) {
    init {
        validate()
    }

    private fun validate() {
        if (items.isEmpty()) throw OrderException.ReciptIsEmpty()
    }

    companion object {
        fun from(releaseItems: List<Product.ReleaseInfo>) = CreateReceipt(items = releaseItems.map {
            CreateOrderItem.from(it)
        })
    }
}


//생성 경로를 ReleaseVO로 제한합니다.
//따라서 해당 클래스에서 검증하는 역할은 수행하지 않습니다.
class CreateOrderItem private constructor(
    val productId: Long,
    val name: String,
    val priceOfOne: BigDecimal,
    val quantity: Long,
) {
    init {
        validate()
    }

    private fun validate() {
        if (quantity < 1L) throw OrderException.OrderItemIsGreaterThanZero()
    }

    companion object {
        fun from(releaseItem: Product.ReleaseInfo) = CreateOrderItem(
            productId = releaseItem.product.id,
            name = releaseItem.product.name,
            priceOfOne = releaseItem.product.price,
            quantity = releaseItem.quantity
        )
    }
}

