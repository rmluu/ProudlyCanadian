package com.example.proudlycanadian.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Purpose: Sets up Retrofit instances for two different APIs: GS1 and UPCItemDB.
 *          Provides access to both services using Retrofit and Moshi for JSON serialization.
 */
object api {
    private const val GS1_BASE_URL = "https://api.gs1us.org/product/trial/Products/v7/"
    private const val UPCITEMDB_BASE_URL = "https://api.upcitemdb.com/prod/trial/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofitGS1 = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(GS1_BASE_URL)
        .build()

    private val retrofitUPC = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(UPCITEMDB_BASE_URL)
        .build()

    val gs1Service: GS1ProductService by lazy {
        retrofitGS1.create(GS1ProductService::class.java)
    }

    val upcService: UPCProductService by lazy {
        retrofitUPC.create(UPCProductService::class.java)
    }
}