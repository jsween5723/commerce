package kr.hhplus.be.server.domain.order.coupon

import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany
import java.math.BigDecimal
import java.util.*

@Embeddable
class UsedCoupons(
    @OneToMany(
        cascade = [(CascadeType.ALL)], orphanRemoval = true
    ) val items: List<OrderCoupon> = LinkedList()
) {
    fun discount(target: BigDecimal): BigDecimal {
        var result = target
        items.forEach {
            result = it.discount(result)
        }
        return result
    }
}