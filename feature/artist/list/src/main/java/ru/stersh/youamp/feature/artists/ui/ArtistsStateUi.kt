package ru.stersh.youamp.feature.artists.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class ArtistsStateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val items: List<ArtistUi> = emptyList()
)