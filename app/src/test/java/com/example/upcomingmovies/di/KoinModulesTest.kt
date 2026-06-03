package com.example.upcomingmovies.di

import android.content.Context
import com.example.upcomingmovies.feature.moviedetails.di.movieDetailModule
import com.example.upcomingmovies.feature.movielist.di.movieListModule
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import retrofit2.Retrofit

@OptIn(KoinExperimentalAPI::class)
class KoinModulesTest {

    @Test
    fun `movieListModule - all bindings can be resolved`() {
        movieListModule.verify(extraTypes = listOf(Context::class))
    }

    @Test
    fun `movieDetailModule - all bindings can be resolved when Retrofit is provided`() {
        // Retrofit is provided at runtime by movieListModule — declared as extraType here
        movieDetailModule.verify(extraTypes = listOf(Retrofit::class))
    }
}
