package ru.stersh.youamp.feature.artist.domain

internal interface ArtistFavoriteRepository {

    suspend fun setFavorite(id: String, isFavorite: Boolean)
}