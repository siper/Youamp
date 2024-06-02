package ru.stersh.youamp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stersh.youamp.core.api.ApiDefaults
import ru.stersh.youamp.core.api.SubsonicApi
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.core.api.provider.NoActiveServerSettingsFound
import ru.stersh.youamp.core.room.server.SubsonicServerDao
import ru.stersh.youamp.core.room.server.SubsonicServerDb

internal class ApiProviderImpl(
    private val subsonicServerDao: SubsonicServerDao
) : ApiProvider {
    private val mutex = Mutex()
    private var apiSonic: SubsonicApi? = null

    override suspend fun getApi(): SubsonicApi = mutex.withLock {
        val currentServerSettings = subsonicServerDao.getActive() ?: throw NoActiveServerSettingsFound()
        return@withLock requireApi(currentServerSettings)
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
                getApiOrNull(it)
            }
    }

    private fun getApiOrNull(subsonicServer: SubsonicServerDb?): SubsonicApi? {
        if (subsonicServer == null) {
            return null
        }
        return requireApi(subsonicServer)
    }

    private fun requireApi(subsonicServer: SubsonicServerDb): SubsonicApi {
        if (!isSameSettings(subsonicServer)) {
            apiSonic = createNewApi(subsonicServer)
        }
        return requireNotNull(apiSonic)
    }

    private fun createNewApi(subsonicServer: SubsonicServerDb): SubsonicApi {
        return SubsonicApi(
            url = subsonicServer.url,
            username = subsonicServer.username,
            password = subsonicServer.password,
            apiVersion = ApiDefaults.API_VERSION,
            clientId = ApiDefaults.CLIENT_ID,
            useLegacyAuth = subsonicServer.useLegacyAuth
        )
    }

    private fun isSameSettings(subsonicServer: SubsonicServerDb): Boolean {
        val currentApiSonic = apiSonic ?: return false
        return currentApiSonic.username == subsonicServer.username &&
                currentApiSonic.password == subsonicServer.password &&
                currentApiSonic.url == subsonicServer.url &&
                currentApiSonic.useLegacyAuth == subsonicServer.useLegacyAuth
    }
}