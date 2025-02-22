package ru.stersh.youamp.main.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.main.domain.ServerExistRepository
import ru.stresh.youamp.core.db.server.SubsonicServerDao

internal class ServerExistRepositoryImpl(private val serverDao: SubsonicServerDao) :
    ServerExistRepository {

    override suspend fun hasServer(): Flow<Boolean> {
        return serverDao
            .flowActive()
            .map { it != null }
    }
}