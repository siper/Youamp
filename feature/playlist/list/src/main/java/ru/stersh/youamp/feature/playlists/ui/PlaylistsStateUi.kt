package ru.stersh.youamp.feature.playlists.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class PlaylistsStateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val items: List<PlaylistUi> = emptyList()
)