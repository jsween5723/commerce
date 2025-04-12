package kr.hhplus.be.server.infrastructure

import kr.hhplus.be.server.domain.order.DataPlatformSender
import kr.hhplus.be.server.domain.order.Order
import org.springframework.stereotype.Repository

@Repository
class DataPlatformSenderImpl : DataPlatformSender {
    override fun send(Order: Order) {
        TODO("Not yet implemented")
    }
}