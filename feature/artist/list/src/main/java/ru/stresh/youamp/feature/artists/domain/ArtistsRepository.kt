package ru.stresh.youamp.feature.artists.domain

import kotlinx.coroutines.flow.Flow

internal interface ArtistsRepository {
    suspend fun getArtists(): Flow<List<Artist>>
}