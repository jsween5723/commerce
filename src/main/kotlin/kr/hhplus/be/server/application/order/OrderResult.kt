package kr.hhplus.be.server.application.order

class OrderResult {
    data class Create(val orderId: Long)
    data class Pay(val orderId: Long, val paymentId: Long)
    data class Cancel(val orderId: Long, val paymentId: Long)
}