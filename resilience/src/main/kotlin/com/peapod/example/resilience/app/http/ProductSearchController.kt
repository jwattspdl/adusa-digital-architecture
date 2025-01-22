package com.peapod.example.resilience.app.http

import com.peapod.example.resilience.app.http.api.ProductSearchApi
import com.peapod.example.resilience.app.http.model.Product
import com.peapod.example.resilience.app.http.model.SearchRequest
import com.peapod.example.resilience.app.http.model.SearchResponse
import com.peapod.example.resilience.core.model.ProductSearchQuery
import com.peapod.example.resilience.core.model.ProductSearchResults
import com.peapod.example.resilience.core.model.ServiceLocationId
import com.peapod.example.resilience.core.service.ProductSearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductSearchController(@Autowired val productSearchService: ProductSearchService) : ProductSearchApi {

    override fun searchProducts(searchRequest: SearchRequest): ResponseEntity<SearchResponse> {

        val query = covertRequest(searchRequest)
        val results = productSearchService.find(query)
        return ResponseEntity.ok(convertResult(results))
    }

    fun covertRequest(searchRequest: SearchRequest) :  ProductSearchQuery {
        return ProductSearchQuery(ServiceLocationId(searchRequest.serviceLocationId),
            searchRequest.text, searchRequest.limit ?: ProductSearchQuery.DEFAULT_LIMIT)
    }

    fun convertResult(productsSearchResults: ProductSearchResults) : SearchResponse {

        val products = productsSearchResults.products.map {
                    Product(id = it.id.toBigDecimal(), name = it.name, price = it.price)
                }
        return SearchResponse(productsSearchResults.totalFound, products)
    }
}


