package ru.stersh.youamp.feature.personal.domain

internal data class Song(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean
)