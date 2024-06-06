package ru.stersh.youamp.feature.albums.ui

import androidx.compose.runtime.Immutable
import ru.stersh.youamp.core.ui.AlbumUi

@Immutable
data class AlbumsStateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val items: List<AlbumUi> = emptyList()
)