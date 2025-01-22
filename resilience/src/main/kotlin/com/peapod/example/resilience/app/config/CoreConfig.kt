package com.peapod.example.resilience.app.config

import com.peapod.example.resilience.adapters.service.IndexApiProductSearchService
import com.peapod.example.resilience.core.service.ProductSearchService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class CoreConfig {

    @Value("\${product-index.url}")
    lateinit var productSearchURL: String

    @Bean("apiSearchService")
    fun getProductSearchService(webClient: WebClient) : ProductSearchService {
        return IndexApiProductSearchService(webClient)
    }

    @Bean
    fun getWebClient() : WebClient {
        return WebClient.create(productSearchURL)
    }
}