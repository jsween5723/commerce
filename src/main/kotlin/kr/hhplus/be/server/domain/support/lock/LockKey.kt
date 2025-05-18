package kr.hhplus.be.server.domain.support.lock

enum class LockKey {
    COUPON, PRODUCT, POINT, ORDER;
    fun createKey(target: String): String {
        return "${name.lowercase()}:$target"
    }
}