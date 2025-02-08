package ru.stresh.youamp.feature.album.favorites.ui

import androidx.compose.runtime.Immutable

internal data class StateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val data: DataUi? = null
)

@Immutable
internal data class DataUi(
    val albums: List<AlbumUi>
)

internal class AlbumUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?
)