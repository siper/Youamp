package ru.stersh.youamp.shared.favorites

data class Song(
    val id: String,
    val title: String,
    val album: String?,
    val albumId: String?,
    val artist: String?,
    val artistId: String?,
    val artworkUrl: String?,
    val userRating: Int?,
)
