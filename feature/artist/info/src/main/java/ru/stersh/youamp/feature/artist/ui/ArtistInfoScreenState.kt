package ru.stersh.youamp.feature.artist.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class ArtistInfoStateUi(
    val progress: Boolean = true,
    val error: Boolean = true,
    val content: ArtistInfoUi? = null
)

@Immutable
internal data class ArtistInfoUi(
    val artworkUrl: String? = null,
    val name: String,
    val isFavorite: Boolean,
    val albums: List<AlbumUi>
)