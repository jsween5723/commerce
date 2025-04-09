package kr.hhplus.be.server.domain.point

import kr.hhplus.be.server.domain.LockTemplate
import org.springframework.stereotype.Component

@Component
class PointLockTemplate : LockTemplate() {
    override fun createKey(key: Any): String {
        return "$PREFIX:$key"
    }

    companion object {
        private const val PREFIX = "UserPoint"
    }
}