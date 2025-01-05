package ru.stersh.youamp.feature.album.domain

internal data class AlbumInfo(
    val artworkUrl: String?,
    val title: String,
    val artist: String,
    val year: String?,
    val songs: List<AlbumSong>
)