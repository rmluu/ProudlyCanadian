package com.example.proudlycanadian.api

import com.example.proudlycanadian.api.model.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Purpose: Defines a method for fetching product details from the GS1 API.
 */
interface GS1ProductService {
    @POST("GetProductsByGTIN")
    fun getProductByGTIN(
        @Body gtinList: List<String>,  // Pass the list of GTINs in the body
        @Header("ApiKey") apiKey: String  // Pass the API key in the header
    ): Call<Product>
}