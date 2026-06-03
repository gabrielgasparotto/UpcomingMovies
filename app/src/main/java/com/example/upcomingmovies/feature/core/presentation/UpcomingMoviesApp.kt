package com.example.upcomingmovies.feature.core.presentation

import android.app.Application
import com.example.upcomingmovies.feature.moviedetails.di.movieDetailModule
import com.example.upcomingmovies.feature.movielist.di.movieListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UpcomingMoviesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@UpcomingMoviesApp)
            modules(movieListModule, movieDetailModule)
        }
    }
}
