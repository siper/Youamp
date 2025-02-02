package ru.stersh.youamp.feature.albums.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class AlbumsStateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val items: List<AlbumUi> = emptyList()
)