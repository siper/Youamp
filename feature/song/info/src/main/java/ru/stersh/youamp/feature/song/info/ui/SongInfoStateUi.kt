package ru.stersh.youamp.feature.song.info.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class SongInfoStateUi(
    val title: String? = null,
    val artist: String? = null,
    val artistId: String? = null,
    val albumId: String? = null,
    val artworkUrl: String? = null,
    val showAlbum: Boolean = false,
    val favorite: Boolean = false,
    val progress: Boolean = true,
    val error: Boolean = false,
)
