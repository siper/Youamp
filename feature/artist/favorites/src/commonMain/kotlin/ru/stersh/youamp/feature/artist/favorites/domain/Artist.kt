package ru.stersh.youamp.feature.artist.favorites.domain

internal data class Artist(
    val id: String,
    val name: String,
    val artworkUrl: String?,
    val userRating: Int?
)
