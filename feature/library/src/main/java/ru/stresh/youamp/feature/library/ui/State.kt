package ru.stresh.youamp.feature.library.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class StateUi(
    val progress: Boolean = true,
    val error: Boolean = false,
    val refreshing: Boolean = false,
    val data: DataUi? = null
)

@Immutable
internal data class DataUi(
    val artists: List<ArtistUi> = emptyList(),
    val albums: List<AlbumUi> = emptyList(),
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