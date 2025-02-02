package ru.stersh.youamp.feature.main.domain

import kotlinx.coroutines.flow.Flow

internal interface ServerInfoRepository {

    fun getServerInfo(): Flow<ServerInfo>
}