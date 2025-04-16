package kr.hhplus.be.server.infrastructure

import kr.hhplus.be.server.domain.order.DataPlatformSender
import kr.hhplus.be.server.domain.order.Order
import org.springframework.stereotype.Component

@Component
class DataPlatformSenderImpl : DataPlatformSender {
    override fun send(order: Order) {
        TODO("Not yet implemented")
    }
}