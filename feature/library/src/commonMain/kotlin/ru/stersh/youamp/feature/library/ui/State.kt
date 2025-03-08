package ru.stersh.youamp.feature.library.ui

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class StateUi(
    val progress: Boolean = true,
    val error: Boolean = false,
    val refreshing: Boolean = false,
    val data: DataUi? = null
)

internal data class DataUi(
    val artists: ImmutableList<ArtistUi> = persistentListOf(),
    val albums: ImmutableList<AlbumUi> = persistentListOf(),
)

internal data class ArtistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?,
    val isPlaying: Boolean
)

internal data class AlbumUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isPlaying: Boolean
)