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
import com.example.upcomingmovies.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
private const val DATABASE_NAME = "upcoming_movies.db"

val movieListModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }
    single { get<AppDatabase>().movieDao() }
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
                        .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}")
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
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    factory { ObserveMoviesUseCase(get()) }
    factory { RefreshMoviesUseCase(get()) }

    viewModel { MovieListViewModel(get(), get()) }
}
