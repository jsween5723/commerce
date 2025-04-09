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
    val product: Product,
    val name: String,
    val priceOfOne: BigDecimal,
    val quantity: Long,
) {
    init {
        validate()
    }

    private fun validate() {
        if (quantity < 1L) throw OrderException.OrderItemIsGreaterThanZero()
//        발생할 수 있는 경우가 없으나 유지보수시 간과하지 않도록 하기위한 방어코드입니다.
        if (name != product.name || priceOfOne != product.price) throw OrderException.OrderItemInfoValidateFail()
    }

    companion object {
        fun from(releaseItem: Product.ReleaseInfo) = CreateOrderItem(
            product = releaseItem.product,
            name = releaseItem.product.name,
            priceOfOne = releaseItem.product.price,
            quantity = releaseItem.quantity
        )
    }
}

