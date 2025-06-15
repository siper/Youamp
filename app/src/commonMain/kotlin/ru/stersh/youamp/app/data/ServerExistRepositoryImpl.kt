package ru.stersh.youamp.app.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.app.domain.ServerExistRepository
import ru.stersh.youamp.core.db.server.SubsonicServerDao

internal class ServerExistRepositoryImpl(
    private val serverDao: SubsonicServerDao,
) : ServerExistRepository {
    override suspend fun hasServer(): Flow<Boolean> =
        serverDao
            .flowActive()
            .map { it != null }
}
