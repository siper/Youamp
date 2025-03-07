package ru.stersh.youamp.feature.artist.favorites.domain

import kotlinx.coroutines.flow.Flow

internal interface FavoriteArtistsRepository {
    fun getFavorites(): Flow<Favorites>
}