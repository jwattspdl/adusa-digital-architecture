package com.peapod.example.resilience.scenarios

import com.intuit.karate.core.MockServer
import com.intuit.karate.junit5.Karate
import com.peapod.example.resilience.ResilienceApplication
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import kotlin.random.Random

@SpringBootTest(classes = [ResilienceApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations=["classpath:inttest.env"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResilienceApplicationTest {

    @LocalServerPort
    lateinit var localServerPort: String

    @Karate.Test
    fun testProductApiResilienceWithIndexErroring() : Karate {
        return setupKarate(localServerPort, "classpath:com/peapod/example/resilience/scenarios/product-api-resilience.feature")
    }

    private fun setupKarate(serverPort: String, vararg paths: String): Karate {
        return addPorts(Karate.run(*paths), serverPort)
    }

    private fun addPorts(karate: Karate, serverPort: String): Karate {
        return karate.systemProperty("spring.port", serverPort)
            .systemProperty("mockServerPort", mockServer.port.toString())
    }

    companion object {

        val mockServerPort: Int = Random.nextInt(9000, 9900)

        val mockServer: MockServer = MockServer.featurePaths(
                "classpath:com/peapod/example/resilience/mocks/product-index.feature",
            ).http(mockServerPort).build()

        @JvmStatic
        @DynamicPropertySource
        fun dynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("PRODUCT_INDEX_URL") {  "http://localhost:${mockServerPort}" }
        }
    }
}