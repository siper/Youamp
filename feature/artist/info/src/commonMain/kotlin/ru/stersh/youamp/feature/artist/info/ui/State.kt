package ru.stersh.youamp.feature.artist.info.ui

import kotlinx.collections.immutable.ImmutableList

internal data class ArtistInfoStateUi(
    val progress: Boolean = true,
    val error: Boolean = true,
    val content: ArtistInfoUi? = null,
)

internal data class ArtistInfoUi(
    val artworkUrl: String? = null,
    val name: String,
    val isFavorite: Boolean,
    val albums: ImmutableList<AlbumUi>,
)

internal data class AlbumUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
)
