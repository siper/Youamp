package ru.stersh.youamp.feature.artist.domain

internal data class ArtistInfo(
    val artworkUrl: String? = null,
    val name: String,
    val albums: List<ArtistAlbum>
)