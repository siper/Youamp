package ru.stersh.youamp.feature.song.favorites.domain

import kotlinx.coroutines.flow.Flow

internal interface FavoriteSongsRepository {
    fun getFavorites(): Flow<Favorites>
}
