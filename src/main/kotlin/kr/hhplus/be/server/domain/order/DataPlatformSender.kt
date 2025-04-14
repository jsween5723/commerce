package kr.hhplus.be.server.domain.order

import org.springframework.stereotype.Component

@Component
interface DataPlatformSender {
    fun send(order: Order)
}