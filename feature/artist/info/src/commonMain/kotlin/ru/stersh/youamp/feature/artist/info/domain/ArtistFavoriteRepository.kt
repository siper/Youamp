package ru.stersh.youamp.feature.artist.info.domain

internal interface ArtistFavoriteRepository {
    suspend fun setFavorite(
        id: String,
        isFavorite: Boolean,
    )
}
