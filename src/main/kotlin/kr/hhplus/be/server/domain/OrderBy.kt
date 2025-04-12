package kr.hhplus.be.server.domain

abstract class OrderBy(open val field: OrderField, open val type: Type) {
    interface OrderField {
        val field: String
    }

    enum class Type {
        DESC, ASC
    }
}