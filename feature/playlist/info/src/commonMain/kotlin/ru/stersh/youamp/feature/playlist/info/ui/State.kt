package ru.stersh.youamp.feature.playlist.info.ui

import kotlinx.collections.immutable.ImmutableList

internal data class PlaylistInfoScreenStateUi(
    val progress: Boolean = true,
    val error: Boolean = false,
    val playlistInfo: PlaylistInfoUi? = null,
)

internal data class PlaylistInfoUi(
    val artworkUrl: String?,
    val title: String,
    val songs: ImmutableList<PlaylistSongUi>,
)

internal data class PlaylistSongUi(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?,
    val isCurrent: Boolean,
    val isPlaying: Boolean,
)
