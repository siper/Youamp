package ru.stersh.youamp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import ru.stersh.subsonic.api.SubsonicApi
import ru.stresh.youamp.core.api.ApiDefaults
import ru.stresh.youamp.core.api.ApiProvider
import ru.stresh.youamp.core.api.NoActiveServerSettingsFound
import ru.stresh.youamp.core.db.server.SubsonicServerDao
import ru.stresh.youamp.core.db.server.SubsonicServerDb
import java.util.concurrent.ConcurrentHashMap

internal class ApiProviderImpl(
    private val subsonicServerDao: SubsonicServerDao
) : ApiProvider {

    private val apiCache = ConcurrentHashMap<Long, SubsonicApi>()

    override suspend fun getApi(): SubsonicApi {
        val currentServerSettings = subsonicServerDao.getActive() ?: throw NoActiveServerSettingsFound()
        return requireApi(currentServerSettings)
    }

    override suspend fun requireApiId(): Long {
        return requireNotNull(getApiId())
    }

    override suspend fun getApiId(): Long? {
        return subsonicServerDao.getActive()?.id
    }

    override fun flowApi(): Flow<SubsonicApi> {
        return subsonicServerDao
            .flowActive()
            .mapNotNull {
                requireApi(it ?: return@mapNotNull null)
            }
    }

    override fun flowApiOrNull(): Flow<SubsonicApi?> {
        return subsonicServerDao
            .flowActive()
            .map {
                if (it == null) {
                    null
                } else {
                    getApi(it.id)
                }
            }
    }

    override fun flowApiId(): Flow<Long?> {
        return subsonicServerDao
            .flowActive()
            .map { it?.id }
    }

    override suspend fun getApi(id: Long): SubsonicApi? {
        return apiCache.getOrPut(id) {
            subsonicServerDao.getServer(id)?.let {
                createNewApi(it)
            }
        }
    }

    override suspend fun requireApi(id: Long): SubsonicApi {
        return requireNotNull(getApi(id))
    }

    private fun requireApi(subsonicServer: SubsonicServerDb): SubsonicApi {
        return apiCache.getOrPut(subsonicServer.id) { createNewApi(subsonicServer) }
    }

    private fun createNewApi(subsonicServer: SubsonicServerDb): SubsonicApi {
        return SubsonicApi(
            baseUrl = subsonicServer.url,
            username = subsonicServer.username,
            password = subsonicServer.password,
            apiVersion = ApiDefaults.API_VERSION,
            clientId = ApiDefaults.CLIENT_ID,
            useLegacyAuth = subsonicServer.useLegacyAuth
        )
    }
}