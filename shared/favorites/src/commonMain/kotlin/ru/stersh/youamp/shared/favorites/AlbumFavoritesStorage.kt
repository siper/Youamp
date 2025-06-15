package ru.stersh.youamp.shared.favorites

import kotlinx.coroutines.flow.Flow

interface AlbumFavoritesStorage {
    fun flowAlbums(): Flow<List<Album>>

    suspend fun getAlbums(): List<Album>

    suspend fun setAlbumFavorite(
        id: String,
        isFavorite: Boolean,
    )
}
