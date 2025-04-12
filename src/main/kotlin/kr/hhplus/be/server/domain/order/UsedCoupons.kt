package kr.hhplus.be.server.domain.order

import jakarta.persistence.CascadeType
import jakarta.persistence.Embeddable
import jakarta.persistence.OneToMany
import java.math.BigDecimal
import java.util.*

@Embeddable
class UsedCoupons protected constructor(
    @OneToMany(
        cascade = [(CascadeType.ALL)], orphanRemoval = true
    ) val items: List<UsedCouponToOrder> = LinkedList()
) {
    fun discount(target: BigDecimal): BigDecimal {
        var result = target
        items.forEach {
            result = it.discount(result)
        }
        return result
    }

    fun deuse() {
        items.forEach { it.deuse() }
    }

    companion object {
        fun from(createUsedCoupons: CreateUsedCoupons) =
            UsedCoupons(createUsedCoupons.coupons.map { UsedCouponToOrder(it) })
    }
}