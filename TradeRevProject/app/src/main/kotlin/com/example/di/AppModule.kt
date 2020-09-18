package com.example.di

import com.example.BuildConfig
import com.example.api.AuthInterceptor
import com.example.api.UnsplashApi
import com.example.repository.UnsplashRepository
import com.example.ui.home.HomeViewModel
import com.example.ui.photo.PhotoViewModel
import com.example.util.PreferenceHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {

    single { PhotoViewModel() }
    single { HomeViewModel(get()) }
}

val networkModule = module {

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.UNSPLASH_API_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun provideOkHttpClient(): OkHttpClient {
        val authInterceptor = AuthInterceptor()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient().newBuilder()
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    fun provideUnsplashApi(retrofit: Retrofit): UnsplashApi = retrofit.create(UnsplashApi::class.java)

    factory { provideOkHttpClient() }
    factory { provideUnsplashApi(get()) }
    single { provideRetrofit(get()) }
}

val repositoryModule = module {
    single { UnsplashRepository(get()) }
}

val prefsModule = module {
    single { PreferenceHelper(get()) }
}

val appModules = listOf(viewModelModule, networkModule, repositoryModule, prefsModule)
