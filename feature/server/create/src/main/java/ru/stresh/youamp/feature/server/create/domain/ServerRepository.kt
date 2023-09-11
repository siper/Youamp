package ru.stresh.youamp.feature.server.create.domain

internal interface ServerRepository {

    suspend fun addServer(server: Server)
}