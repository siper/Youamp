package ru.stresh.youamp.feature.server.list.data

import kotlinx.coroutines.flow.Flow
import ru.stresh.youamp.core.room.server.SubsonicServerDao
import ru.stresh.youamp.core.utils.mapItems
import ru.stresh.youamp.feature.server.list.domain.Server
import ru.stresh.youamp.feature.server.list.domain.ServerListRepository

internal class ServerListRepositoryImpl(private val serverDao: SubsonicServerDao) : ServerListRepository {

    override fun getServerList(): Flow<List<Server>> {
        return serverDao
            .flowAll()
            .mapItems {
                Server(
                    id = it.id,
                    title = it.name,
                    url = it.url,
                    isActive = it.isActive
                )
            }
    }

    override suspend fun setActiveServer(serverId: Long) {
        serverDao.setActive(serverId)
    }

    override suspend fun deleteServer(serverId: Long) {
        serverDao.delete(serverId)
    }
}