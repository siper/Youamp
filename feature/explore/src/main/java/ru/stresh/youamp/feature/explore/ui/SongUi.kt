package ru.stresh.youamp.feature.explore.ui

internal data class SongUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean
)