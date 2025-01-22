package com.peapod.example.resilience.core.service

import com.peapod.example.resilience.core.model.ProductSearchQuery
import com.peapod.example.resilience.core.model.ProductSearchResults

interface ProductSearchService {

    fun find(query: ProductSearchQuery) : ProductSearchResults
}