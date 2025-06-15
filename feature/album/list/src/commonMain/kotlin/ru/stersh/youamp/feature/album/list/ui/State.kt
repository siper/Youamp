package ru.stersh.youamp.feature.album.list.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class AlbumsStateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val items: ImmutableList<AlbumUi> = persistentListOf(),
)

internal data class AlbumUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
)
