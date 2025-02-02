package ru.stersh.youamp.feature.personal.domain

internal data class Artist(
    val id: String,
    val name: String,
    val artworkUrl: String?,
    val isPlaying: Boolean
)