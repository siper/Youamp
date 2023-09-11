package ru.stresh.youamp.main.data

import ru.stresh.youamp.core.room.server.SubsonicServerDao
import ru.stresh.youamp.main.domain.ServerExistRepository

internal class ServerExistRepositoryImpl(private val serverDao: SubsonicServerDao) :
    ServerExistRepository {
    override suspend fun hasServer(): Boolean {
        return serverDao.getCount() > 0
    }
}