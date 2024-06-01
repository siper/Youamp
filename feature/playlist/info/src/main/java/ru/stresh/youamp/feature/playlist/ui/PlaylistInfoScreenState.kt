package ru.stresh.youamp.feature.playlist.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class PlaylistInfoScreenStateUi(
    val progress: Boolean = true,
    val artworkUrl: String? = null,
    val title: String = "",
    val songs: List<PlaylistSongUi> = emptyList()
)