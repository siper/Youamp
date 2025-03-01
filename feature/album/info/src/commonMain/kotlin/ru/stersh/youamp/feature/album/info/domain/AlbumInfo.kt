package ru.stersh.youamp.feature.album.info.domain

internal data class AlbumInfo(
    val artworkUrl: String?,
    val title: String,
    val artist: String,
    val year: String?,
    val isFavorite: Boolean,
    val songs: List<AlbumSong>
) 