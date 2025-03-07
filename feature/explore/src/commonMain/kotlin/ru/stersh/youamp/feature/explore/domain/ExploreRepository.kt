package ru.stersh.youamp.feature.explore.domain

import kotlinx.coroutines.flow.Flow

internal interface ExploreRepository {
    fun getExplore(): Flow<Explore>
    suspend fun refresh()
}