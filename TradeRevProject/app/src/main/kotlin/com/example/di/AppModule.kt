package com.example.di

import com.example.BuildConfig
import com.example.api.GithubApi
import com.example.repository.GithubRepository
import com.example.ui.home.HomeViewModel
import com.example.ui.splash.SplashViewModel
import com.example.util.PreferenceHelper
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {

    single { SplashViewModel() }
    single { HomeViewModel(get()) }
}

val networkModule = module {

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().build()
    }

    fun provideGithubApi(retrofit: Retrofit): GithubApi = retrofit.create(GithubApi::class.java)

    factory { provideOkHttpClient() }
    factory { provideGithubApi(get()) }
    single { provideRetrofit(get()) }
}

val repositoryModule = module {
    single { GithubRepository(get()) }
}

val prefsModule = module {
    single { PreferenceHelper(get()) }
}

val appModules = listOf(viewModelModule, networkModule, repositoryModule, prefsModule)
