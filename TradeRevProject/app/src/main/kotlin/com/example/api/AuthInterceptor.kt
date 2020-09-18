package com.example.api

import com.example.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val CLIENT_ID_KEY = "Client-ID"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader(
            AUTHORIZATION_HEADER,
            "$CLIENT_ID_KEY ${BuildConfig.UNSPLASH_ACCESS_KEY}"
        )

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}
