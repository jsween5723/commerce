package kr.hhplus.be.server.domain.support.lock

enum class LockKey(val createKey: (target: String) -> String) {
    COUPON({ "coupon:${it}" }), PRODUCT({ "product:${it}}" }), POINT({ "point:${it}" });
}