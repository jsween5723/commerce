package kr.hhplus.be.server

import jakarta.persistence.EntityManagerFactory
import kr.hhplus.be.server.application.order.OrderMapper
import kr.hhplus.be.server.domain.auth.Authentication
import kr.hhplus.be.server.domain.auth.UserId
import kr.hhplus.be.server.domain.coupon.Coupon
import kr.hhplus.be.server.domain.order.CreateOrder
import kr.hhplus.be.server.domain.order.Order
import kr.hhplus.be.server.domain.point.UserPoint
import kr.hhplus.be.server.domain.product.Product
import kr.hhplus.be.server.domain.product.RankedProduct
import org.hibernate.SessionFactory
import org.instancio.Instancio
import org.instancio.Select.field
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntegrationTestSupport {
    @Autowired
    private lateinit var couponRepository: JpaRepository<Coupon, Long>

    @Autowired
    private lateinit var orderRepository: JpaRepository<Order, Long>

    @Autowired
    private lateinit var productRepository: JpaRepository<Product, Long>

    @Autowired
    private lateinit var entityManagerFactory: EntityManagerFactory

    @Autowired
    private lateinit var rankedProductRepository: JpaRepository<RankedProduct, Long>

    @Autowired
    private lateinit var pointRepository: JpaRepository<UserPoint, Long>
    private val longFixture = LongFixture()

    @BeforeAll
    fun beforeEach(): Unit {
        entityManagerFactory.unwrap(SessionFactory::class.java).withStatelessOptions().openStatelessSession().use {
            val tx = it.beginTransaction()
            pointRepository.saveAll(IntRange(0, 20000).map {
                Instancio.of(UserPoint::class.java)
                    .supply(field("point")) { gen ->
                        gen.longRange(1000000, 20000000).toBigDecimal()
                    }
                    .supply(field("userId")) { gen ->
                        UserId(longFixture.userId())
                    }
                    .ignore(field("id"))
                    .create()
            })
            val coupons = IntRange(0, 100).map {
                Instancio.of(Coupon::class.java).supply(field("discountAmount")) { _ ->
                    val number = Random.nextInt(100)
                    BigDecimal(number)
                }.generate(field("expireDuration")) { gen ->
                    gen.temporal().duration().min(100, ChronoUnit.DAYS)
                }.generate(field("publishFrom")) { gen -> gen.temporal().localDateTime().past() }
                    .generate(field("publishTo")) { gen -> gen.temporal().localDateTime().future() }
                    .ignore(field("id"))
                    .create()
            }
            val saved = couponRepository.saveAll(coupons)

            val products = IntRange(0, 100).map {
                Instancio.of(Product::class.java)
                    .generate(field("stockNumber")) { gen -> gen.longs().min(500) }
                    .supply(
                        field("price")
                    ) { _ -> BigDecimal.valueOf(Random.nextInt(300, 3000).toLong()) }
                    .ignore(field("id")).create()
            }
            val saveProducts = productRepository.saveAll(products)
            orderRepository.saveAll(saveProducts.map { it.release(15) }
                .map { re -> OrderMapper.toSelectedProductAndQuantitySnapshot(re) }
                .chunked(4).map { items -> CreateOrder(items, Authentication(longFixture.userId())).toOrder() })
            val longFixture = LongFixture()
            val rankedProducts = IntRange(0, 100).map {
                Instancio.of(RankedProduct::class.java).set(field("productId"), longFixture.productId())
                    .ignore(field("id"))
                    .generate(field("totalSellingCount")) { gen -> gen.longs().min(1).max(10000) }
                    .supply(field("totalIncome")) { gen -> BigDecimal(Random.nextInt(0, 10000)) }
                    .supply(field("createdDate")) { gen -> LocalDate.now() }
                    .create()
            }
            rankedProductRepository.saveAll(rankedProducts)
            tx.commit()
        }
    }
}

