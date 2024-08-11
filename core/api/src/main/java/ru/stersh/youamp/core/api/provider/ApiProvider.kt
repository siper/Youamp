package ru.stersh.youamp.core.api.provider

import kotlinx.coroutines.flow.Flow
import ru.stersh.youamp.core.api.SubsonicApi

interface ApiProvider {

    suspend fun getApi(): SubsonicApi

    suspend fun getApiId(): Long?

    suspend fun requireApiId(): Long

    fun flowApi(): Flow<SubsonicApi>

    fun flowApiOrNull(): Flow<SubsonicApi?>
}
