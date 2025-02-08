package ru.stersh.youamp.feature.artists.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class ArtistsStateUi(
    val progress: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Boolean = false,
    val items: ImmutableList<ArtistUi> = persistentListOf()
)

internal data class ArtistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?
)