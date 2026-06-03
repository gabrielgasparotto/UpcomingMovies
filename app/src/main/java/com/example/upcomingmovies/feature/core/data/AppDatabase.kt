package com.example.upcomingmovies.feature.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.upcomingmovies.feature.movielist.data.local.MovieDao
import com.example.upcomingmovies.feature.movielist.data.local.MovieEntity

const val DATABASE_VERSION = 1

@Database(entities = [MovieEntity::class], version = DATABASE_VERSION, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}