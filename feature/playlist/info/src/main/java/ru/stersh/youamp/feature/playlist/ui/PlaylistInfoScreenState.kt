package ru.stersh.youamp.feature.playlist.ui

import androidx.compose.runtime.Immutable

@Immutable
internal data class PlaylistInfoScreenStateUi(
    val progress: Boolean = true,
    val error: Boolean = false,
    val playlistInfo: PlaylistInfoUi? = null
)

@Immutable
internal data class PlaylistInfoUi(
    val artworkUrl: String?,
    val title: String,
    val songs: List<PlaylistSongUi>
)