package ru.stersh.youamp.feature.album.ui

import kotlinx.collections.immutable.ImmutableList

internal data class AlbumInfoStateUi(
    val progress: Boolean = true,
    val content: AlbumInfoUi? = null,
    val error: Boolean = false
)

internal data class AlbumInfoUi(
    val artworkUrl: String?,
    val title: String,
    val artist: String,
    val year: String?,
    val isFavorite: Boolean,
    val songs: ImmutableList<AlbumSongUi>
)

internal data class AlbumSongUi(
    val id: String,
    val title: String,
    val track: Int?,
    val artist: String?,
    val duration: String
)