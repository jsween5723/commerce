package kr.hhplus.be.server.domain.coupon

import org.springframework.stereotype.Repository

@Repository
interface CouponRepository {
    fun findPublishedByUserId(userId: Long): List<PublishedCoupon>
    fun findPublishedByIdsForSelect(ids: List<Long>): List<PublishedCoupon>
    fun findById(id: Long): Coupon?
    fun findByIdForPublish(id: Long): Coupon?
    fun save(publishedCoupon: PublishedCoupon): PublishedCoupon
}