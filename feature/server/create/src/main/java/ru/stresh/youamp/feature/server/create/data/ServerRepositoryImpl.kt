package ru.stresh.youamp.feature.server.create.data

import ru.stresh.youamp.core.api.ApiDefaults
import ru.stresh.youamp.core.api.SubsonicApi
import ru.stresh.youamp.core.room.server.SubsonicServerDao
import ru.stresh.youamp.core.room.server.SubsonicServerDb
import ru.stresh.youamp.feature.server.create.domain.Server
import ru.stresh.youamp.feature.server.create.domain.ServerRepository

internal class ServerRepositoryImpl(
    private val serverDao: SubsonicServerDao,
) : ServerRepository {
    override suspend fun addServer(server: Server) {
        val isActive = serverDao.getActive() == null
        serverDao.insert(server.toData(isActive))
    }

    override suspend fun editServer(serverId: Long, server: Server) {
        val newServer = serverDao
            .getServer(serverId)
            ?.copy(
                name = server.name,
                url = server.url,
                username = server.username,
                password = server.password,
                useLegacyAuth = server.useLegacyAuth
            )
            ?: return
        serverDao.insert(newServer)
    }

    override suspend fun getServer(serverId: Long): Server? {
        return serverDao
            .getServer(serverId)
            ?.let {
                Server(
                    name = it.name,
                    url = it.url,
                    username = it.username,
                    password = it.password,
                    useLegacyAuth = it.useLegacyAuth
                )
            }
    }

    override suspend fun hasActiveServer(): Boolean {
        return serverDao.getActive() != null
    }

    override suspend fun testServer(server: Server) {
        val pingResponse = SubsonicApi(
            url = server.url,
            username = server.username,
            password = server.password,
            apiVersion = ApiDefaults.API_VERSION,
            clientId = ApiDefaults.CLIENT_ID,
            useLegacyAuth = server.useLegacyAuth
        ).ping()

        if (pingResponse.status != "ok") {
            error("Test server not success")
        }
    }

    private fun Server.toData(isActive: Boolean): SubsonicServerDb {
        return SubsonicServerDb(
            name = name,
            url = url,
            username = username,
            password = password,
            useLegacyAuth = useLegacyAuth,
            isActive = isActive
        )
    }
}