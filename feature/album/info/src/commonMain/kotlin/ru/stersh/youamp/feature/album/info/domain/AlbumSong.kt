package ru.stersh.youamp.feature.album.info.domain

internal data class AlbumSong(
    val id: String,
    val track: Int?,
    val title: String,
    val artist: String?,
    val duration: String
)