package ru.stresh.youamp

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stresh.youamp.core.api.ApiDefaults
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
        return@withLock if (isSameSettings(currentServerSettings)) {
            requireNotNull(apiSonic)
        } else {
            apiSonic = createNewApi(currentServerSettings)
            requireNotNull(apiSonic)
        }
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