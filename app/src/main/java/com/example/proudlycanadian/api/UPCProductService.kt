package com.example.proudlycanadian.api

import com.example.proudlycanadian.api.model.UPCProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Purpose: Defines a method for fetching product details from the UPCItemDB API using a UPC code.
 */
interface UPCProductService {
    @GET("lookup")
    fun getProductByUPC(
        @Query("upc") upc: String
    ): Call<UPCProductResponse>
}