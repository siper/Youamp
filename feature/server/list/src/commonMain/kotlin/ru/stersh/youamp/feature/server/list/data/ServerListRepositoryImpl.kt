package ru.stersh.youamp.feature.server.list.data

import kotlinx.coroutines.flow.Flow
import ru.stersh.youamp.core.db.server.SubsonicServerDao
import ru.stersh.youamp.core.utils.mapItems
import ru.stersh.youamp.feature.server.list.domain.Server
import ru.stersh.youamp.feature.server.list.domain.ServerListRepository

internal class ServerListRepositoryImpl(
    private val serverDao: SubsonicServerDao,
) : ServerListRepository {
    override fun getServerList(): Flow<List<Server>> =
        serverDao
            .flowAll()
            .mapItems {
                Server(
                    id = it.id,
                    title = it.name,
                    url = it.url,
                    isActive = it.isActive,
                )
            }

    override suspend fun setActiveServer(serverId: Long) {
        serverDao.setActive(serverId)
    }

    override suspend fun deleteServer(serverId: Long) {
        serverDao.delete(serverId)
    }
}
