package com.example.upcomingmovies.di

import android.content.Context
import com.example.upcomingmovies.feature.core.di.networkModule
import com.example.upcomingmovies.feature.moviedetails.di.movieDetailModule
import com.example.upcomingmovies.feature.movielist.di.movieListModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import retrofit2.Retrofit

@OptIn(KoinExperimentalAPI::class)
class KoinModulesTest {

    @Test
    fun `networkModule - all bindings can be resolved`() {
        networkModule.verify()
    }

    @Test
    fun `movieListModule - all bindings can be resolved`() {
        // Retrofit is provided at runtime by networkModule — declared as extraType here
        movieListModule.verify(extraTypes = listOf(Context::class, Retrofit::class))
    }

    @Test
    fun `movieDetailModule - all bindings can be resolved`() {
        // Retrofit is provided at runtime by networkModule — declared as extraType here
        movieDetailModule.verify(extraTypes = listOf(Retrofit::class))
    }
}
