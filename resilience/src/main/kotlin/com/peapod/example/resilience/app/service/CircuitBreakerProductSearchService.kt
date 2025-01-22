package com.peapod.example.resilience.app.service

import com.peapod.example.resilience.core.model.ProductSearchQuery
import com.peapod.example.resilience.core.model.ProductSearchResults
import com.peapod.example.resilience.core.service.ProductSearchService
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service("productSearchService")
class CircuitBreakerProductSearchService(

    @Autowired @Qualifier("apiSearchService")
    val productSearchService: ProductSearchService) : ProductSearchService {

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "emptyResults")
    @Retry(name = CIRCUIT_BREAKER_NAME)
    override fun find(query: ProductSearchQuery): ProductSearchResults {
        return productSearchService.find(query)
    }

    fun emptyResults(query: ProductSearchQuery, ex: Exception): ProductSearchResults {
        logger.error("${CIRCUIT_BREAKER_NAME} - Error searching for.")
        return ProductSearchResults(0, emptyList())
    }

    companion object {
        const val CIRCUIT_BREAKER_NAME = "productApi"
        val logger = LoggerFactory.getLogger(CircuitBreakerProductSearchService::class.java)
    }

}