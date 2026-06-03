package com.example.upcomingmovies.feature.movielist.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.upcomingmovies.feature.core.data.AppDatabase
import com.example.upcomingmovies.feature.movielist.data.remote.MovieService
import com.example.upcomingmovies.feature.movielist.data.repository.MovieRepositoryImpl
import com.example.upcomingmovies.feature.movielist.domain.repository.MovieRepository
import com.example.upcomingmovies.feature.movielist.domain.usecase.ObserveMoviesUseCase
import com.example.upcomingmovies.feature.movielist.domain.usecase.RefreshMoviesUseCase
import com.example.upcomingmovies.feature.movielist.presentation.viewmodel.MovieListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

private const val DATABASE_NAME = "upcoming_movies.db"

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE movies ADD COLUMN voteCount INTEGER NOT NULL DEFAULT 0")
    }
}

val movieListModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME,
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }
    single { get<AppDatabase>().movieDao() }
    single { get<Retrofit>().create(MovieService::class.java) }
    single<MovieRepository> { MovieRepositoryImpl(get(), get()) }
    factory { ObserveMoviesUseCase(get()) }
    factory { RefreshMoviesUseCase(get()) }

    viewModel { MovieListViewModel(get(), get()) }
}
