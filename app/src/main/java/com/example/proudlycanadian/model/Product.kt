package com.example.proudlycanadian.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Purpose: Defines data models for the product details fetched from the GS1 API.
 */
@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "products")
    var products: List<ProductX>?
)

@JsonClass(generateAdapter = true)
data class ProductX(
    @Json(name = "gtin")
    var gtin: String?,
    @Json(name = "isComplete")
    var isComplete: Boolean?,
    @Json(name = "gtinRecordStatus")
    var gtinRecordStatus: String?,
    @Json(name = "gs1Licence")
    var gs1Licence: Gs1Licence?
)

@JsonClass(generateAdapter = true)
data class Gs1Licence(
    @Json(name = "licenceKey")
    var licenceKey: String?,
    @Json(name = "licenceType")
    var licenceType: String?,
    @Json(name = "licenseeName")
    var licenseeName: String?,
    @Json(name = "licenseeGLN")
    var licenseeGLN: String?,
    @Json(name = "licensingMO")
    var licensingMO: LicensingMO?,
    @Json(name = "address")
    var address: Address?
)

@JsonClass(generateAdapter = true)
data class LicensingMO(
    @Json(name = "moName")
    var moName: String?
)

@JsonClass(generateAdapter = true)
data class Address(
    @Json(name = "streetAddress")
    var streetAddress: StreetAddress?,
    @Json(name = "addressLocality")
    var addressLocality: AddressLocality?,
    @Json(name = "countryCode")
    var countryCode: String?,
    @Json(name = "postalName")
    var postalName: PostalName?,
    @Json(name = "addressRegion")
    var addressRegion: AddressRegion?,
    @Json(name = "postalCode")
    var postalCode: String?
)

@JsonClass(generateAdapter = true)
data class AddressLocality(
    @Json(name = "language")
    var language: String?,
    @Json(name = "value")
    var value: String?
)

@JsonClass(generateAdapter = true)
data class AddressRegion(
    @Json(name = "language")
    var language: String?,
    @Json(name = "value")
    var value: String?
)

@JsonClass(generateAdapter = true)
data class PostalName(
    @Json(name = "language")
    var language: String?,
    @Json(name = "value")
    var value: String?
)

@JsonClass(generateAdapter = true)
data class StreetAddress(
    @Json(name = "language")
    var language: String?,
    @Json(name = "value")
    var value: String?
)


