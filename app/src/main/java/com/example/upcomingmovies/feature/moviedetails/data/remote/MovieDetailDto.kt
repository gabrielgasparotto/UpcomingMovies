package com.example.upcomingmovies.feature.moviedetails.data.remote

import com.google.gson.annotations.SerializedName

data class MovieDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("status") val status: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("runtime") val runtime: Int?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("video") val video: Boolean,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("budget") val budget: Long,
    @SerializedName("revenue") val revenue: Long,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("genres") val genres: List<GenreDto>,
    @SerializedName("origin_country") val originCountry: List<String>,
)

data class GenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
)
