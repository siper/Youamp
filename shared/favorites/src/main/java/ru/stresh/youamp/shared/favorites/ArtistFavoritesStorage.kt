package ru.stresh.youamp.shared.favorites

import kotlinx.coroutines.flow.Flow

interface ArtistFavoritesStorage {

    fun flowArtists(): Flow<List<Artist>>

    suspend fun getArtists(): List<Artist>

    suspend fun setArtistFavorite(id: String, isFavorite: Boolean)
}