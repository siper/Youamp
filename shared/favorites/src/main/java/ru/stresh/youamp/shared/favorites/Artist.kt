package ru.stresh.youamp.shared.favorites

data class Artist(
    val id: String,
    val name: String,
    val artworkUrl: String?,
    val userRating: Int?
)