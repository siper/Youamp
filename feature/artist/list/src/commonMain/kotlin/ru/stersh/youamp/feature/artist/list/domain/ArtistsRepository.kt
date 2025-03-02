package ru.stersh.youamp.feature.artist.list.domain

import kotlinx.coroutines.flow.Flow

internal interface ArtistsRepository {
    suspend fun getArtists(): Flow<List<Artist>>
}