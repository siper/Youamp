package ru.stersh.youamp.core.api

import kotlinx.coroutines.flow.Flow
import ru.stersh.subsonic.api.SubsonicApi

interface ApiProvider {

    suspend fun getApi(): SubsonicApi

    suspend fun getApiId(): Long?

    suspend fun requireApiId(): Long

    suspend fun getApi(id: Long): SubsonicApi?

    suspend fun requireApi(id: Long): SubsonicApi

    fun flowApi(): Flow<SubsonicApi>

    fun flowApiOrNull(): Flow<SubsonicApi?>

    fun flowApiId(): Flow<Long?>
}
