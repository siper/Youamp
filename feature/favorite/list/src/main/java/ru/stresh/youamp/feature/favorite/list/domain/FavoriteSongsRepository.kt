package ru.stresh.youamp.feature.favorite.list.domain

import kotlinx.coroutines.flow.Flow

internal interface FavoriteSongsRepository {
    fun getFavorites(): Flow<Favorites>
}