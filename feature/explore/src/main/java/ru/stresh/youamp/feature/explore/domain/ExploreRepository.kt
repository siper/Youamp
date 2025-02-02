package ru.stresh.youamp.feature.explore.domain

import kotlinx.coroutines.flow.Flow

internal interface ExploreRepository {
    fun getExplore(): Flow<Explore>
}