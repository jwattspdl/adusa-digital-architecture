package com.peapod.example.resilience.adapters.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.peapod.example.resilience.core.model.Product
import com.peapod.example.resilience.core.model.ProductSearchQuery
import com.peapod.example.resilience.core.model.ProductSearchResults
import com.peapod.example.resilience.core.service.ProductSearchService
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono


class IndexApiProductSearchService(val webClient : WebClient) : ProductSearchService {

    override fun find(query: ProductSearchQuery): ProductSearchResults {

        try {
            val results = webClient.post()
                .uri("/${query.serviceLocationId.id}/search")
                .header("Content-Type", "application/json")
                .bodyValue("{ \"searchText\":\"${query.queryText}\" }")
                .retrieve()
                .onStatus({it.value() == 404},
                    {
                       Mono.error(NoProductsFoundException())
                    })
                .bodyToMono(Results::class.java)
                .block()

            if (results == null || results.totalFound == 0) {
                return ProductSearchResults(0, emptyList())
            }

            val resultProducts = results.products.map { it.toProduct() }
            return ProductSearchResults(results.totalFound, resultProducts)

        } catch (e: NoProductsFoundException) {
            logger.debug("No products found for search query: ${query.queryText}")
            return ProductSearchResults(0, emptyList())
        } catch (e: NullPointerException) {
            logger.error("Invalid product search result from product API. Missing id, name, or price.", e)
            return ProductSearchResults(0, emptyList())
        } catch (e: Exception) {
            logger.error("Error searching for products", e)
            throw e
        }
    }

    companion object {
        val logger = LoggerFactory.getLogger(IndexApiProductSearchService::class.java)
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Results(var totalFound: Int = 0, var products: List<P> = emptyList())

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class P(val productId: Int? = null, val name: String? = null, val price: Float? = null) {

    fun toProduct() : Product {
        return Product(productId!!, name!!, price!!)
    }
}

class NoProductsFoundException : RuntimeException()
