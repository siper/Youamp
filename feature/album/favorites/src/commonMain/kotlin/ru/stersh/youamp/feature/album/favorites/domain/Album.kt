package ru.stersh.youamp.feature.album.favorites.domain

internal data class Album(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val userRating: Int?,
)
