package com.example.proudlycanadian.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Purpose: Models the response from the UPCItemDB API for product data.
 */
@JsonClass(generateAdapter = true)
data class UPCProductResponse(
    @Json(name = "items") val items: List<UPCProduct>?
)

/**
 * Purpose: Represents a product retrieved from the UPCItemDB API.
 */
@JsonClass(generateAdapter = true)
data class UPCProduct(
    @Json(name = "ean") val ean: String?,
    @Json(name = "upc") val upc: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "brand") val brand: String?,
    @Json(name = "category") val category: String?,
    @Json(name = "images") val images: List<String>?
)