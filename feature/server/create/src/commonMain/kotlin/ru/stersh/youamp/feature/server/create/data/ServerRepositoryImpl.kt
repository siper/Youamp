package ru.stersh.youamp.feature.server.create.data

import ru.stersh.subsonic.api.SubsonicApi
import ru.stersh.youamp.core.api.ApiDefaults
import ru.stersh.youamp.core.db.server.SubsonicServerDao
import ru.stersh.youamp.core.db.server.SubsonicServerDb
import ru.stersh.youamp.feature.server.create.domain.Server
import ru.stersh.youamp.feature.server.create.domain.ServerRepository

internal class ServerRepositoryImpl(
    private val serverDao: SubsonicServerDao,
) : ServerRepository {
    override suspend fun addServer(server: Server) {
        val isActive = serverDao.getActive() == null
        serverDao.insert(server.toData(isActive))
    }

    override suspend fun editServer(
        serverId: Long,
        server: Server,
    ) {
        val newServer =
            serverDao
                .getServer(serverId)
                ?.copy(
                    name = server.name,
                    url = server.url,
                    username = server.username,
                    password = server.password,
                    authType = server.authType.name,
                )
                ?: return
        serverDao.insert(newServer)
    }

    override suspend fun getServer(serverId: Long): Server? =
        serverDao
            .getServer(serverId)
            ?.let {
                Server(
                    name = it.name,
                    url = it.url,
                    username = it.username,
                    password = it.password,
                    authType = authTypeFromString(it.authType),
                )
            }

    private fun authTypeFromString(value: String): Server.AuthType =
        when (value) {
            "Unsecure" -> Server.AuthType.Unsecure
            "EncodedPassword" -> Server.AuthType.EncodedPassword
            else -> Server.AuthType.Token
        }

    override suspend fun hasActiveServer(): Boolean = serverDao.getActive() != null

    override suspend fun testServer(server: Server) {
        val pingResponse =
            SubsonicApi(
                baseUrl = server.url,
                username = server.username,
                password = server.password,
                apiVersion = ApiDefaults.API_VERSION,
                clientId = ApiDefaults.CLIENT_ID,
                authType = server.authType.toSubsonicApiAuthType(),
            ).ping().data

        if (pingResponse.status != "ok") {
            error("Test server not success")
        }
    }

    private fun Server.toData(isActive: Boolean): SubsonicServerDb =
        SubsonicServerDb(
            name = name,
            url = url,
            username = username,
            password = password,
            authType = authType.name,
            isActive = isActive,
        )

    private fun Server.AuthType.toSubsonicApiAuthType(): ru.stersh.subsonic.api.AuthType =
        when (this) {
            Server.AuthType.Unsecure -> {
                ru.stersh.subsonic.api.AuthType.Unsecure
            }

            Server.AuthType.EncodedPassword -> {
                ru.stersh.subsonic.api.AuthType.EncodedPassword
            }

            Server.AuthType.Token -> {
                ru.stersh.subsonic.api.AuthType
                    .Token()
            }
        }
}
