package kr.hhplus.be.server

import jakarta.persistence.EntityManagerFactory
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.coupon.CouponCommand
import kr.hhplus.be.server.domain.coupon.CouponService
import kr.hhplus.be.server.domain.coupon.PublishedCoupon
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.order.OrderCommand
import kr.hhplus.be.server.domain.order.OrderService
import kr.hhplus.be.server.domain.order.coupon.CouponSnapshot
import kr.hhplus.be.server.domain.order.product.ProductSnapshot
import kr.hhplus.be.server.domain.point.PointCommand
import kr.hhplus.be.server.domain.point.PointService
import kr.hhplus.be.server.domain.point.UserPoint
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.ProductQuery
import kr.hhplus.be.server.domain.product.ProductService
import kr.hhplus.be.server.domain.product.RankedProduct
import org.hibernate.SessionFactory
import org.instancio.Instancio
import org.instancio.Select.field
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

@SpringBootTest
class IntegrationTestSupport {
    private lateinit var rankedProducts: List<RankedProduct>
    private lateinit var coupons: List<Coupon>

    @Autowired
    lateinit var productService: ProductService

    @Autowired
    lateinit var orderService: OrderService

    @Autowired
    lateinit var pointService: PointService

    @Autowired
    lateinit var couponService: CouponService

    companion object {
        const val MAX_COUNT = 10L
        const val MAX_STOCK_NUMBER = 1000L
        const val MAX_COUPON_STOCK_NUMBER = 100L
        const val ZERO_STOCK_ID_MOD = 2
        const val MAX_COUPON_DURATION = 5L
    }

    @Autowired
    lateinit var couponRepository: JpaRepository<Coupon, Long>

    @Autowired
    lateinit var orderRepository: JpaRepository<Order, Long>

    @Autowired
    lateinit var productRepository: JpaRepository<Product, Long>

    @Autowired
    lateinit var entityManagerFactory: EntityManagerFactory

    @Autowired
    lateinit var rankedProductRepository: JpaRepository<RankedProduct, Long>

    @Autowired
    lateinit var pointRepository: JpaRepository<UserPoint, Long>
    protected val idGenerator = LongFixture()
    protected lateinit var products: List<Product>

    fun insertTemplate(entities: Collection<Any>) {
        val it =
            entityManagerFactory.unwrap(SessionFactory::class.java).withStatelessOptions().openStatelessSession()
        val tx = it.beginTransaction()
        try {
            entities.map { entity -> it.insert(entity) }
            tx.commit()
        } catch (e: Exception) {
            tx.rollback()
        } finally {
            it.close()
        }
    }

    fun insertProducts() {
        products = LongRange(0, MAX_COUNT).map {
            Instancio.of(Product::class.java)
                .generate(field("stockNumber")) { gen -> gen.longs().min(500).max(1000) }
                .supply(
                    field("price")
                ) { _ -> BigDecimal.valueOf(Random.nextInt(300, 3000).toLong()) }
                .ignore(field("id")).create()
        }
        insertTemplate(
            products
        )
    }

    fun insertRankedProducts() {
        val longFixture = LongFixture()
        rankedProducts = LongRange(0, MAX_COUNT).map {
            Instancio.of(RankedProduct::class.java).set(field("productId"), longFixture.productId())
                .ignore(field("id"))
                .generate(field("totalSellingCount")) { gen -> gen.longs().min(1).max(10000) }
                .supply(field("totalIncome")) { gen -> BigDecimal(Random.nextInt(0, 10000)) }
                .supply(field("createdDate")) { gen -> LocalDate.now() }
                .create()
        }
        insertTemplate(rankedProducts)
    }

    fun insertCoupons() {
        coupons = LongRange(0, MAX_COUNT).map {
            Instancio.of(Coupon::class.java).supply(field("discountAmount")) { _ ->
                val number = Random.nextLong(100)
                BigDecimal(number)
            }.generate(field("expireDuration")) { gen ->
                gen.temporal().duration().min(1, ChronoUnit.DAYS).max(MAX_COUPON_DURATION, ChronoUnit.DAYS)
            }.generate(field("publishFrom")) { gen -> gen.temporal().localDateTime().past() }
                .generate(field("publishTo")) { gen -> gen.temporal().localDateTime().future() }
                .supply(field("stock")) { gen ->
                    if (it % 2 == 0.toLong()) 0 else gen.longRange(
                        1,
                        MAX_COUPON_STOCK_NUMBER
                    )
                }
                .ignore(field("id"))
                .create()

        }
        insertTemplate(coupons)
    }

    protected fun 포인트를_조회한다(userId: UserId): UserPoint {
        return pointService.findByUserId(userId)
    }

    protected fun 포인트를_사용한다(userId: UserId, amount: BigDecimal): UserPoint {
        val chargeAmount = amount
        return pointService.use(
            PointCommand.Use(
                amount = chargeAmount,
                userId = userId,
                authentication = Authentication(userId = userId.userId, isSuper = false)
            )
        )
    }

    protected fun 포인트를_충전한다(userId: UserId, amount: BigDecimal) {
        val chargeAmount = amount
        pointService.charge(
            PointCommand.Charge(
                amount = chargeAmount,
                userId = userId,
                authentication = Authentication(userId = userId.userId, isSuper = false)
            )
        )
    }


    protected fun 사용자의_쿠폰_목록을_조회한다(userId: Long): List<PublishedCoupon> {
        return couponService.findPublishedByUserId(userId)
    }

    protected fun 대상_쿠폰_목록을_사용한다(userId: Long, publishCouponIds: List<Long>): List<PublishedCoupon> {
        val command = CouponCommand.Select(Authentication(userId), publishCouponIds)
        return couponService.selectPublishedCoupons(command)
    }

    protected fun 주문한다(
        userId: Long,
        publishCoupons: List<CouponSnapshot>,
        productIdAndQuantities: List<ProductSnapshot>
    ): Order {
        val command = OrderCommand.Create(
            selectedProducts = productIdAndQuantities,
            publishedCoupons = publishCoupons,
            authentication = Authentication(userId = userId)
        )
        return orderService.create(command)
    }

    protected fun 주문을_결제한다(
        userId: Long,
        order: Order,
    ) {
        order.pay(Authentication(userId))
    }

    protected fun 주문을_취소한다(
        userId: Long,
        order: Order,
    ) {
        order.cancel(Authentication(userId))
    }

    protected fun 상품목록을_조회한다(): List<Product> {
        return productService.findAll()
    }

    protected fun 인기_상품목록을_조회한다(query: ProductQuery.Ranked): List<RankedProduct> {
        return productService.findRankedBy(query)
    }

    protected fun 쿠폰을_발급한다(userId: Long, couponId: Long): PublishedCoupon {
        val command = CouponCommand.Publish(Authentication(userId), couponId)
        return couponService.publish(command)
    }
}

