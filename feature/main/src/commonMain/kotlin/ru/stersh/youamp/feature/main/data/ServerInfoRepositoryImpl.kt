package ru.stersh.youamp.feature.main.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.core.db.server.SubsonicServerDao
import ru.stersh.youamp.feature.main.domain.ServerInfo
import ru.stersh.youamp.feature.main.domain.ServerInfoRepository

internal class ServerInfoRepositoryImpl(
    private val subsonicServerDao: SubsonicServerDao,
    private val apiProvider: ApiProvider,
) : ServerInfoRepository {
    override fun getServerInfo(): Flow<ServerInfo> =
        combine(
            subsonicServerDao
                .flowActive()
                .filterNotNull(),
            apiProvider.flowApi(),
        ) { active, api ->
            ServerInfo(
                name = active.name,
                avatarUrl =
                    api
                        .avatarUrl(
                            active.username,
                            true,
                        ).toString(),
            )
        }
}
