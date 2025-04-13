package kr.hhplus.be.server.domain.order.product

import java.math.BigDecimal

fun ProductSnapshotFixture(quantity: Long = 2000, price: BigDecimal = BigDecimal.ONE): ProductSnapshot =
    ProductSnapshot(
        id = 7142,
        name = "Emma Randolph",
        quantity = quantity,
        price = price,
    )