package kr.hhplus.be.server.domain.order.product

import java.math.BigDecimal

fun ProductSnapshotFixture(
    quantity: Long = 2000,
    price: BigDecimal = BigDecimal.ONE,
    productId: Long = 1L
): ProductSnapshot =
    ProductSnapshot(
        id = productId,
        name = "Emma Randolph",
        quantity = quantity,
        price = price,
    )