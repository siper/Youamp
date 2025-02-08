package ru.stresh.youamp.shared.favorites

data class Album(
    val id: String,
    val title: String,
    val artist: String?,
    val artistId: String?,
    val artworkUrl: String?,
    val userRating: Int?
)
