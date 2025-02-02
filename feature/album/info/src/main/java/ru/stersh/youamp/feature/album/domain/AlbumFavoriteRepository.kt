package ru.stersh.youamp.feature.album.domain

internal interface AlbumFavoriteRepository {

    suspend fun setFavorite(id: String, isFavorite: Boolean)
}