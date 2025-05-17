package kr.hhplus.be.server.domain.support.lock

enum class LockKey(val single: (target: String) -> String) {
    COUPON({ "coupon:${it}" }), PRODUCT({ "coupon:${it}}" }), POINT({ "point:${it}" }),
}