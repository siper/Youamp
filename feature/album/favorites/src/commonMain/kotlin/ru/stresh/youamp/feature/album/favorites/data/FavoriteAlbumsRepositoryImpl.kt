package ru.stresh.youamp.feature.album.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stresh.youamp.feature.album.favorites.domain.Album
import ru.stresh.youamp.feature.album.favorites.domain.FavoriteAlbumsRepository
import ru.stresh.youamp.feature.album.favorites.domain.Favorites
import ru.stresh.youamp.shared.favorites.AlbumFavoritesStorage

internal class FavoriteAlbumsRepositoryImpl(
    private val albumFavoritesStorage: AlbumFavoritesStorage
) : FavoriteAlbumsRepository {

    override fun getFavorites(): Flow<Favorites> {
        return albumFavoritesStorage
            .flowAlbums()
            .map { favoriteAlbums ->
                Favorites(
                    albums = favoriteAlbums.map { it.toDomain() }
                )
            }
    }

    private fun ru.stresh.youamp.shared.favorites.Album.toDomain(): Album {
        return Album(
            id = id,
            title = title,
            artist = artist,
            artworkUrl = artworkUrl,
            userRating = userRating
        )
    }
}