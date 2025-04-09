package kr.hhplus.be.server.domain.product

class ProductFixture(
    id: Long = 1L,
    name: String = "Blanca",
    price: Long = 0,
    stockNumber: Long = 0
) :
    Product(id = id, name = name, price = price, stockNumber = stockNumber)