package ru.stresh.youamp.feature.artist.favorites.ui

import androidx.compose.runtime.Immutable

internal data class StateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val data: DataUi? = null
)

@Immutable
internal data class DataUi(
    val artists: List<ArtistUi>
)

internal class ArtistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?
)