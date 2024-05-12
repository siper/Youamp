package ru.stresh.youamp.feature.search.domain

import kotlinx.coroutines.flow.Flow

internal interface SearchRepository {

    fun searchResults(): Flow<SearchResult>

    suspend fun search(query: String)

    suspend fun loadMoreSongs()

    suspend fun loadMoreAlbums()

    suspend fun loadMoreArtists()
}