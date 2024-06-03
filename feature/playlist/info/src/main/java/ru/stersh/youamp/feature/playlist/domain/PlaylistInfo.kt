package ru.stersh.youamp.feature.playlist.domain

internal data class PlaylistInfo(
    val artworkUrl: String?,
    val title: String,
    val songs: List<PlaylistSong>
)