package kr.hhplus.be.server.domain.coupon

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponService(private val couponRepository: CouponRepository) {
    fun findById(id: Long) = couponRepository.findById(id) ?: throw CouponException.CouponNotFound()
    fun findPublishedByUserId(userId: Long) = couponRepository.findPublishedByUserId(userId)
    fun findPublishedByIds(ids: List<Long>) = couponRepository.findPublishedByIds(ids)

    @Transactional
    fun publish(command: CouponCommand.Publish): PublishedCoupon {
        val (authentication, couponId) = command
        val coupon = couponRepository.findByIdForPublish(couponId) ?: throw CouponException.CouponNotFound()
        return couponRepository.save(coupon.publish(authentication.id, LocalDateTime.now()))
    }

    @Transactional
    fun selectPublishedCoupons(command: CouponCommand.Select): List<PublishedCoupon> {
        val (authentication, couponIds) = command
        val publishedCoupons = couponRepository.findPublishedByIds(couponIds)
        publishedCoupons.forEach { it.use(LocalDateTime.now(), authentication) }
        return publishedCoupons
    }
}