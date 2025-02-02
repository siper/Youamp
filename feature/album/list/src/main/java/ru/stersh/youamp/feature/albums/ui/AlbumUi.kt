package ru.stersh.youamp.feature.albums.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class AlbumUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?
)