package ru.stersh.youamp.main.data

import ru.stersh.youamp.core.room.server.SubsonicServerDao
import ru.stersh.youamp.main.domain.ServerExistRepository

internal class ServerExistRepositoryImpl(private val serverDao: SubsonicServerDao) :
    ServerExistRepository {
    override suspend fun hasServer(): Boolean {
        return serverDao.getCount() > 0
    }
}