package kr.hhplus.be.server.domain.coupon

import kr.hhplus.be.server.domain.support.lock.DistributedLock
import kr.hhplus.be.server.domain.support.lock.LockKey
import kr.hhplus.be.server.domain.support.lock.LockType
import org.springframework.dao.ConcurrencyFailureException
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponService(private val couponRepository: CouponRepository) {
    fun findById(id: Long) = couponRepository.findById(id) ?: throw CouponException.CouponNotFound()
    fun findPublishedByUserId(userId: Long) = couponRepository.findPublishedByUserId(userId)
    fun findPublishedByIds(ids: List<Long>) = couponRepository.findPublishedByIdsForSelect(ids)

    @Transactional
    @DistributedLock("#command.couponId", domain = LockKey.COUPON, type = LockType.REDIS_PUBSUB)
    fun publish(command: CouponCommand.Publish): PublishedCoupon {
        val (authentication, couponId) = command
        val coupon = couponRepository.findByIdForPublish(couponId) ?: throw CouponException.CouponNotFound()
        return couponRepository.save(coupon.publish(authentication.id, LocalDateTime.now()))
    }

    @Recover
    fun recover(e: Exception): List<PublishedCoupon> = throw CouponException.PleaseAgainLater()

    @Transactional
    @Retryable(
        retryFor = [ConcurrencyFailureException::class],
        maxAttempts = 1,
    )
    fun selectPublishedCoupons(command: CouponCommand.Select): List<PublishedCoupon> {
        val (authentication, couponIds) = command
        val publishedCoupons = couponRepository.findPublishedByIdsForSelect(couponIds)
        publishedCoupons.forEach { it.use(LocalDateTime.now(), authentication) }
        return publishedCoupons
    }
}