package kr.hhplus.be.server.domain.order

interface DataPlatformSender {
    fun send(order: Order)
}