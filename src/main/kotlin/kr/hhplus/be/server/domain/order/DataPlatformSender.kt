package kr.hhplus.be.server.domain.order

interface DataPlatformSender {
    fun send(Order: Order)
}