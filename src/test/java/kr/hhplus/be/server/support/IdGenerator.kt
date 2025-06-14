package kr.hhplus.be.server.support

import org.springframework.boot.test.context.TestComponent
import java.util.concurrent.atomic.AtomicLong

@TestComponent
class IdGenerator {
    private var orderId = AtomicLong(1)
    private var couponId = AtomicLong(1)
    private var userId = AtomicLong(1)
    private var productId = AtomicLong(1)
    fun orderId(): Long = orderId.getAndIncrement()
    fun userId(): Long = userId.getAndIncrement()
    fun productId(): Long = productId.getAndIncrement()
    fun couponId(): Long = couponId.getAndIncrement()
}