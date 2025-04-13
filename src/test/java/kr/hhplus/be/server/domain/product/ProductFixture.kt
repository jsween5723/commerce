package kr.hhplus.be.server.domain.product

import java.math.BigDecimal

class ProductFixture(
    id: Long = 1L, name: String = "Blanca", price: BigDecimal = BigDecimal.ZERO, stockNumber: Long = 0
) : Product(id = id, name = name, price = price, stockNumber = stockNumber)

