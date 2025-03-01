package ru.stersh.youamp.feature.album.info.data

import ru.stersh.youamp.feature.album.info.domain.AlbumFavoriteRepository
import ru.stresh.youamp.shared.favorites.AlbumFavoritesStorage

internal class AlbumFavoriteRepositoryImpl(
    private val albumFavoritesStorage: AlbumFavoritesStorage
) : AlbumFavoriteRepository {

    override suspend fun setFavorite(id: String, isFavorite: Boolean) {
        albumFavoritesStorage.setAlbumFavorite(id, isFavorite)
    }
}