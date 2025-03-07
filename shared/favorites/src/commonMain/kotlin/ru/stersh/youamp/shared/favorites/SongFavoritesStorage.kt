package ru.stersh.youamp.shared.favorites

import kotlinx.coroutines.flow.Flow

interface SongFavoritesStorage {

    fun flowSongs(): Flow<List<Song>>

    suspend fun getSongs(): List<Song>

    suspend fun setSongFavorite(id: String, isFavorite: Boolean)
}