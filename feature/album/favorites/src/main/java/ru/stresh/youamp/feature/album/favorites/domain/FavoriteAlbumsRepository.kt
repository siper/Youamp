package ru.stresh.youamp.feature.album.favorites.domain

import kotlinx.coroutines.flow.Flow

internal interface FavoriteAlbumsRepository {
    fun getFavorites(): Flow<Favorites>
}