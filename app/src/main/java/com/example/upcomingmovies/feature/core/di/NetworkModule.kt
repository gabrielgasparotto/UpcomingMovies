package com.example.upcomingmovies.feature.core.di

import com.example.upcomingmovies.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
private const val HEADER_AUTHORIZATION = "Authorization"
private const val HEADER_ACCEPT = "Accept"
private const val HEADER_ACCEPT_VALUE = "application/json"
private const val BEARER_PREFIX = "Bearer "

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader(HEADER_AUTHORIZATION, "$BEARER_PREFIX${BuildConfig.TMDB_ACCESS_TOKEN}")
                        .addHeader(HEADER_ACCEPT, HEADER_ACCEPT_VALUE)
                        .build()
                )
            }
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
