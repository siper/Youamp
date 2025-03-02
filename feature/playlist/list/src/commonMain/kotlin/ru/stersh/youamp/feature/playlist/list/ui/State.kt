package ru.stersh.youamp.feature.playlist.list.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class PlaylistsStateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val items: ImmutableList<PlaylistUi> = persistentListOf()
)

internal data class PlaylistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?
)