package ru.stresh.youamp

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stresh.youamp.core.api.SubsonicApi
import ru.stresh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.core.api.provider.NoActiveServerSettingsFound
import ru.stresh.youamp.core.room.server.SubsonicServerDao
import ru.stresh.youamp.core.room.server.SubsonicServerDb

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
            apiVersion = API_VERSION,
            clientId = CLIENT_ID,
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

    companion object {
        private const val API_VERSION = "1.15.0"
        private const val CLIENT_ID = "YouAMP"
    }
}