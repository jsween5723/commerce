package kr.hhplus.be.server.domain.product

import kr.hhplus.be.server.domain.order.product.SelectedProductSnapshot
import java.math.BigDecimal

class ProductFixture(
    id: Long = 1L, name: String = "Blanca", price: BigDecimal = BigDecimal.ZERO, stockNumber: Long = 0
) : Product(id = id, name = name, price = price, stockNumber = stockNumber)

fun ProductSnapshotFixture(quantity: Long = 2000, price: BigDecimal = BigDecimal.ONE): SelectedProductSnapshot =
    SelectedProductSnapshot(
        id = 7142,
        name = "Emma Randolph",
        quantity = quantity,
        price = price,
    )