package ru.stersh.youamp.feature.playlists.domain

internal data class Playlist(
    val id: String,
    val name: String,
    val artworkUrl: String?
)