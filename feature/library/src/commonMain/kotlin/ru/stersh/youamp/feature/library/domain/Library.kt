package ru.stersh.youamp.feature.library.domain

internal data class Library(
    val artists: List<Artist>,
    val albums: List<Album>,
)

internal data class Artist(
    val id: String,
    val name: String,
    val artworkUrl: String?,
    val isPlaying: Boolean
)

internal data class Album(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean
)