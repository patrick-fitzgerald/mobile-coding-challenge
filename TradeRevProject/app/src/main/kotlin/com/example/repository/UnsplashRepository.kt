package com.example.repository

import com.example.api.Resource
import com.example.api.UnsplashApi
import com.example.data.response.UnsplashPhoto
import timber.log.Timber

class UnsplashRepository constructor(
    private val unsplashApi: UnsplashApi
) {

    suspend fun getPhotos(pageNumber: Int): Resource<List<UnsplashPhoto>> {
        return try {
            val response = unsplashApi.photos(pageNumber = pageNumber.toString()) // TODO setup pagination
            Timber.d("getPhotos response=$response")
            Resource.success(data = response)
        } catch (exception: Exception) {
            Timber.e("getPhotos exception=$exception")
            Resource.error(msg = exception.message ?: "An error occurred", data = null)
        }
    }
}
