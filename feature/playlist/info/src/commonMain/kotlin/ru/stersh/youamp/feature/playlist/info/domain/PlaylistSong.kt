package ru.stersh.youamp.feature.playlist.info.domain

internal data class PlaylistSong(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isCurrent: Boolean,
    val isPlaying: Boolean
)