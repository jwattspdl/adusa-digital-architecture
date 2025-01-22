package com.peapod.example.resilience.core.model

data class ProductSearchQuery(val serviceLocationId: ServiceLocationId, val queryText: String, val limit: Int) {
    companion object {
        const val DEFAULT_LIMIT = 10
    }
}

