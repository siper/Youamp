package ru.stersh.youamp.feature.artist.info.domain

internal data class ArtistInfo(
    val artworkUrl: String? = null,
    val isFavorite: Boolean,
    val name: String,
    val albums: List<ArtistAlbum>
)