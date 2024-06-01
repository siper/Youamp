package ru.stersh.youamp.feature.playlists.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class PlaylistUi(
    val id: String,
    val name: String,
    val artworkUrl: String?
)