package kr.hhplus.be.server.domain.product

import java.math.BigDecimal

fun ProductFixture(
    name: String = "Blanca", price: BigDecimal = 10.toBigDecimal(), stockNumber: Long = 30L
) = Product(name = name, price = price, stockNumber = stockNumber)

