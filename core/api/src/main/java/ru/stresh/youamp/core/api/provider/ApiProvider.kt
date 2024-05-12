package ru.stresh.youamp.core.api.provider

import kotlinx.coroutines.flow.Flow
import ru.stresh.youamp.core.api.SubsonicApi

interface ApiProvider {

    suspend fun getApi(): SubsonicApi

    fun flowApi(): Flow<SubsonicApi>
}
