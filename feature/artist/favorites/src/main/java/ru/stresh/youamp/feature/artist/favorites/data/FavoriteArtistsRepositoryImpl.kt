package ru.stresh.youamp.feature.artist.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stresh.youamp.feature.artist.favorites.domain.Artist
import ru.stresh.youamp.feature.artist.favorites.domain.FavoriteArtistsRepository
import ru.stresh.youamp.feature.artist.favorites.domain.Favorites
import ru.stresh.youamp.shared.favorites.ArtistFavoritesStorage

internal class FavoriteArtistsRepositoryImpl(
    private val artistFavoritesStorage: ArtistFavoritesStorage
) : FavoriteArtistsRepository {

    override fun getFavorites(): Flow<Favorites> {
        return artistFavoritesStorage
            .flowArtists()
            .map { favoriteAlbums ->
                Favorites(
                    artists = favoriteAlbums.map { it.toDomain() }
                )
            }
    }

    private fun ru.stresh.youamp.shared.favorites.Artist.toDomain(): Artist {
        return Artist(
            id = id,
            name = name,
            artworkUrl = artworkUrl,
            userRating = userRating
        )
    }
}