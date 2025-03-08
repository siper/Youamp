package ru.stersh.youamp.feature.artist.favorites.ui

import kotlinx.collections.immutable.ImmutableList

internal data class StateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val data: DataUi? = null
)

internal data class DataUi(
    val artists: ImmutableList<ArtistUi>
)

internal class ArtistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?
)