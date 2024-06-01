package ru.stersh.youamp.core.api.provider

import kotlinx.coroutines.flow.Flow
import ru.stersh.youamp.core.api.SubsonicApi

interface ApiProvider {

    suspend fun getApi(): SubsonicApi

    fun flowApi(): Flow<SubsonicApi>
}
