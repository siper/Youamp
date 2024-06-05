package ru.stersh.youamp.feature.artists.domain

internal data class Artist(
    val id: String,
    val name: String,
    val albumCount: Int?,
    val artworkUrl: String?
)