package com.example.newsapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBusinessNews(@Query("apiKey") header: String, @Query("country") country : String, @Query("category") cat : String) : Response<NewsResponse>
}