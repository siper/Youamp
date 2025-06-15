package ru.stersh.youamp.feature.server.create.domain

internal interface ServerRepository {
    suspend fun addServer(server: Server)

    suspend fun editServer(
        serverId: Long,
        server: Server,
    )

    suspend fun getServer(serverId: Long): Server?

    suspend fun hasActiveServer(): Boolean

    suspend fun testServer(server: Server)
}
