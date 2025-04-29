package com.example.proudlycanadian.api

import android.util.Log
import com.example.proudlycanadian.api.model.Product
import com.example.proudlycanadian.api.model.UPCProductResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.proudlycanadian.BuildConfig

/**
 * Purpose: Manages fetching product details by GTIN from both GS1 and UPCItemDB APIs.
 */
class ProductManager {
    // Function to get product by GTIN and return specific details (gtin and country code)
    fun getProductDetails(gtin: String, onProductFetched: (String, String, String, String, String) -> Unit) {
        val apiKey = BuildConfig.API_KEY
        val requestBody = listOf(gtin)

        // First API call to GS1 API to get country of origin
        val service = api.gs1Service.getProductByGTIN(requestBody, apiKey)

        service.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    // Ensure the response contains product data
                    val product = response.body()?.products?.firstOrNull()
                    if (product != null) {
                        // Extract country code from the product
                        val countryCode = product.gs1Licence?.address?.countryCode ?: "Unknown"
                        // Call UPC API to get additional product details (title & image)
                        fetchUPCData(gtin, countryCode, onProductFetched)
                    } else {
                        // Handle case when the product is not found
                        Log.e("API", "No product data found for GTIN: $gtin")
                        onProductFetched("No product found", "Unknown", "No name", "No image", "No UPC")
                    }
                } else {
                    Log.e("API", "API Error: ${response.code()} - ${response.message()}")
                    onProductFetched("Error fetching product", "Unknown", "No Name", "No image", "No UPC")
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.e("API", "GS1 API Call Failed: ${t.message}", t)
                onProductFetched("Error fetching product", "Unknown", "No name", "No image", "No UPC")
            }
        })
    }

    // Second API call to UPCItemDB API to get UPC, product name, and image
    private fun fetchUPCData(gtin: String, countryCode: String, onProductFetched: (String, String, String, String, String) -> Unit) {
        val service = api.upcService.getProductByUPC(gtin)

        service.enqueue(object : Callback<UPCProductResponse> {
            override fun onResponse(call: Call<UPCProductResponse>, response: Response<UPCProductResponse>) {
                if (response.isSuccessful) {
                    val product = response.body()?.items?.firstOrNull()  // Correctly access 'items' here

                    val productTitle = product?.title ?: "No name"
                    val productImage = product?.images?.firstOrNull() ?: "No image"
                    val upcNumber = product?.upc ?: product?.ean ?: "No UPC" // Extrac UPC or EAN number

                    // Return the details from both APIs
                    onProductFetched(gtin, countryCode, productTitle, productImage, upcNumber)
                } else {
                    Log.e("API", "UPC API Error: ${response.code()} - ${response.message()}")
                    onProductFetched(gtin, countryCode, "No title", "No image", "No UPC")
                }
            }

            override fun onFailure(call: Call<UPCProductResponse>, t: Throwable) {
                Log.e("API", "UPC API Call Failed: ${t.message}", t)
                onProductFetched(gtin, countryCode, "No title", "No image", "No UPC")
            }
        })
    }
}