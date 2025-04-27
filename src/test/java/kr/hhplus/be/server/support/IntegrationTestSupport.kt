package kr.hhplus.be.server.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.restassured.RestAssured
import io.restassured.config.ObjectMapperConfig.objectMapperConfig
import jakarta.persistence.EntityManagerFactory
import org.hibernate.SessionFactory
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = ["kr.hhplus.be.server"])
@Import(TestcontainersConfiguration::class)
class IntegrationTestSupport {

    @LocalServerPort
    var port = 0

    @Autowired
    private lateinit var databaseCleanup: DatabaseCleanup

    @Autowired
    lateinit var entityManagerFactory: EntityManagerFactory

    @Autowired
    lateinit var idGenerator: IdGenerator

    @Autowired
    lateinit var objectMapper: ObjectMapper

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

    @BeforeEach
    fun setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT || RestAssured.port == 8080) {
            RestAssured.port = port
            databaseCleanup.afterPropertiesSet()
            RestAssured.config = RestAssured.config()
                .objectMapperConfig(
                    objectMapperConfig()
                        .jackson2ObjectMapperFactory { cls, charset ->
                            objectMapper.registerModule(KotlinModule.Builder().build())
                            objectMapper
                        })
        }

        println(port)
        databaseCleanup.execute()
    }
}

fun concurrentlyRun(
    action: Array<() -> Any>
) {
    val latch = CountDownLatch(action.size)
    val pool = Executors.newFixedThreadPool(action.size)
    action.map { ac ->
        pool.submit {
            try {
                ac()
            } finally {
                latch.countDown()
            }
        }
    }
    latch.await()
    pool.shutdown()
}