package com.example.upcomingmovies.feature.movielist.di

import androidx.room.Room
import com.example.upcomingmovies.feature.core.data.AppDatabase
import com.example.upcomingmovies.feature.movielist.data.remote.MovieService
import com.example.upcomingmovies.feature.movielist.data.repository.MovieRepositoryImpl
import com.example.upcomingmovies.feature.movielist.domain.repository.MovieRepository
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveMoviesUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.RefreshMoviesUseCase
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"

// Replace with your TMDB API Read Access Token from https://www.themoviedb.org/settings/api
private const val TMDB_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MTBjNDgxZTU1YWE1MmI2YTE2ZTIyODg1ZDNjNTBkNiIsIm5iZiI6MTc4MDQyMDU4Ni4yNTIsInN1YiI6IjZhMWYwZmVhZTNiM2UyNDY0YTBkMTk2MSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.kULTot1PtDDgQo8aPtYo6ZHPqzq7VEWIoa1-aCSKHTw"

val movieListModule = module {
    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "upcoming_movies.db",
        ).build()
    }
    single { get<AppDatabase>().movieDao() }

    // Network
    single {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $TMDB_ACCESS_TOKEN")
                        .addHeader("Accept", "application/json")
                        .build()
                )
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
    single { get<Retrofit>().create(MovieService::class.java) }

    // Data
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    factory { ObserveMoviesUseCase(get()) }
    factory { RefreshMoviesUseCase(get()) }

    // ViewModel
    viewModel { MovieListViewModel(get(), get()) }
}
