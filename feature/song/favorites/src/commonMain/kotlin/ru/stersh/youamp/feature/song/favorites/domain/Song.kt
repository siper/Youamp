package ru.stersh.youamp.feature.song.favorites.domain

internal data class Song(
    val id: String,
    val title: String,
    val artist: String?,
    val album: String?,
    val artworkUrl: String?,
    val userRating: Int?,
)
