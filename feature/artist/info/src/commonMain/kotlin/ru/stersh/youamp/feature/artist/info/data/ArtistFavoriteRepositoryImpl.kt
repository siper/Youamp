package ru.stersh.youamp.feature.artist.info.data

import ru.stersh.youamp.feature.artist.info.domain.ArtistFavoriteRepository
import ru.stresh.youamp.shared.favorites.ArtistFavoritesStorage

internal class ArtistFavoriteRepositoryImpl(
    private val artistFavoritesStorage: ArtistFavoritesStorage
) : ArtistFavoriteRepository {

    override suspend fun setFavorite(id: String, isFavorite: Boolean) {
        artistFavoritesStorage.setArtistFavorite(id, isFavorite)
    }
}