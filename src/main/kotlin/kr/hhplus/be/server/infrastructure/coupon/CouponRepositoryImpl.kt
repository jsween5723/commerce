package kr.hhplus.be.server.infrastructure.coupon

import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.CouponRepository
import kr.hhplus.be.server.domain.coupon.PublishedCoupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl(
    private val couponJpaRepository: CouponJpaRepository,
    private val publishedCouponRepository: PublishedCouponRepository
) : CouponRepository {
    override fun findPublishedByUserId(userId: Long): List<PublishedCoupon> {
        return couponJpaRepository.findPublishedByUserId(userId)
    }

    override fun findPublishedByIds(ids: List<Long>): List<PublishedCoupon> {
        return couponJpaRepository.findPublishedByIds(ids)
    }

    override fun findById(id: Long): Coupon? {
        return couponJpaRepository.findByIdOrNull(id)
    }

    override fun save(publishedCoupon: PublishedCoupon): PublishedCoupon {
        return publishedCouponRepository.save(publishedCoupon)
    }
}

@Repository
interface CouponJpaRepository : JpaRepository<Coupon, Long> {
    @Query("select pc from published_coupons pc join fetch coupons c on pc.coupon = c where pc.userId.userId = :userId")
    fun findPublishedByUserId(userId: Long): List<PublishedCoupon>

    @Query("select pc from published_coupons pc where pc.userId.userId in (:ids)")
    fun findPublishedByIds(ids: List<Long>): List<PublishedCoupon>
}

@Repository
interface PublishedCouponRepository : JpaRepository<PublishedCoupon, Long>