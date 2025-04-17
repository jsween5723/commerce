package kr.hhplus.be.server

import java.util.concurrent.atomic.AtomicLong

class LongFixture {
    private var orderId = AtomicLong(1)
    private var couponId = AtomicLong(1)
    private var userId = AtomicLong(1)
    private var productId = AtomicLong(1)
    fun orderId(): Long = orderId.getAndIncrement()
    fun userId(): Long = userId.getAndIncrement()
    fun productId(): Long = productId.getAndIncrement()
    fun couponId(): Long = couponId.getAndIncrement()
}