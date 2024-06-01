package ru.stersh.youamp.feature.playlist.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class PlaylistSongUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isCurrent: Boolean,
    val isPlaying: Boolean
)