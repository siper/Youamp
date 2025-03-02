package ru.stersh.youamp.feature.artist.info.domain

internal data class ArtistAlbum(
    val id: String,
    val title: String,
    val artist: String?,
    val artworkUrl: String?
)