package ru.stresh.youamp.feature.server.create.data

import ru.stresh.youamp.core.room.server.SubsonicServerDao
import ru.stresh.youamp.core.room.server.SubsonicServerDb
import ru.stresh.youamp.feature.server.create.domain.Server
import ru.stresh.youamp.feature.server.create.domain.ServerRepository

internal class ServerRepositoryImpl(private val serverDao: SubsonicServerDao) : ServerRepository {
    override suspend fun addServer(server: Server) {
        serverDao.insert(server.toData())
    }

    private fun Server.toData(): SubsonicServerDb {
        return SubsonicServerDb(
            name = name,
            url = url,
            username = username,
            password = password,
            useLegacyAuth = useLegacyAuth,
            isActive = true
        )
    }
}