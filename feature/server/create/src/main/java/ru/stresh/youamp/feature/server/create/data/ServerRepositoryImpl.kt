package ru.stresh.youamp.feature.server.create.data

import ru.stresh.youamp.core.room.server.SubsonicServerDao
import ru.stresh.youamp.core.room.server.SubsonicServerDb
import ru.stresh.youamp.feature.server.create.domain.Server
import ru.stresh.youamp.feature.server.create.domain.ServerRepository

internal class ServerRepositoryImpl(private val serverDao: SubsonicServerDao) : ServerRepository {
    override suspend fun addServer(server: Server) {
        val isActive = serverDao.getActive() == null
        serverDao.insert(server.toData(isActive))
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