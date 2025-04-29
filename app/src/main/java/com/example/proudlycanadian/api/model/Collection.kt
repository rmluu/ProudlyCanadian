package com.example.proudlycanadian.api.model

/**
 * Purpose: Represents a product collection in Firestore, which includes a list of products.
 */
data class Collection(
    val name: String,
    val products: List<FirestoreProduct> = listOf()
)