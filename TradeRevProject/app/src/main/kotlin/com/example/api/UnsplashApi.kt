package com.example.api

import com.example.data.response.UnsplashPhoto
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos")
    suspend fun photos(@Query("page") pageNumber: String, @Query("per_page") perPage: String): List<UnsplashPhoto>
}
