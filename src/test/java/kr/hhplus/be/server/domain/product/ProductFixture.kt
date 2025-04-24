package kr.hhplus.be.server.domain.product

import java.math.BigDecimal

fun ProductFixture(
    id: Long = 1L, name: String = "Blanca", price: BigDecimal = 10.toBigDecimal(), stockNumber: Long = 30L
) = Product(id = id, name = name, price = price, stockNumber = stockNumber)

