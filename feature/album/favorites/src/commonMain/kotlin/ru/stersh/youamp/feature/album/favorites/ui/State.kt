package ru.stersh.youamp.feature.album.favorites.ui

import kotlinx.collections.immutable.ImmutableList

internal data class StateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val data: DataUi? = null,
)

internal data class DataUi(
    val albums: ImmutableList<AlbumUi>,
)

internal class AlbumUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
)
