package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.CouponRepository
import kr.hhplus.be.server.domain.coupon.PublishedCoupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl(private val couponJpaRepository: CouponJpaRepository) : CouponRepository {
    override fun findPublishedByUserId(userId: Long): List<PublishedCoupon> {
        return couponJpaRepository.findPublishedByUserId(userId)
    }

    override fun findPublishedByIds(ids: List<Long>): List<PublishedCoupon> {
        return couponJpaRepository.findPublishedByIds(ids)
    }

    override fun findById(id: Long): Coupon? {
        return couponJpaRepository.findByIdOrNull(id)
    }
}

interface CouponJpaRepository : JpaRepository<Coupon, Long> {
    @Query("select pc from published_coupons pc where pc.userId.userId = :userId")
    fun findPublishedByUserId(userId: Long): List<PublishedCoupon>

    @Query("select pc from published_coupons pc where pc.userId.userId in (:userIds)")
    fun findPublishedByIds(ids: List<Long>): List<PublishedCoupon>
}