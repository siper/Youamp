package ru.stersh.youamp.feature.album.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.feature.album.favorites.domain.Album
import ru.stersh.youamp.feature.album.favorites.domain.FavoriteAlbumsRepository
import ru.stersh.youamp.feature.album.favorites.domain.Favorites
import ru.stersh.youamp.shared.favorites.AlbumFavoritesStorage

internal class FavoriteAlbumsRepositoryImpl(
    private val albumFavoritesStorage: AlbumFavoritesStorage,
) : FavoriteAlbumsRepository {
    override fun getFavorites(): Flow<Favorites> =
        albumFavoritesStorage
            .flowAlbums()
            .map { favoriteAlbums ->
                Favorites(
                    albums = favoriteAlbums.map { it.toDomain() },
                )
            }

    private fun ru.stersh.youamp.shared.favorites.Album.toDomain(): Album =
        Album(
            id = id,
            title = title,
            artist = artist,
            artworkUrl = artworkUrl,
            userRating = userRating,
        )
}
