package ru.stersh.youamp.shared.player.favorites

import kotlinx.coroutines.flow.Flow

interface CurrentSongFavorites {
    fun toggleFavorite(favorite: Boolean)
    fun isFavorite(): Flow<Boolean>
}