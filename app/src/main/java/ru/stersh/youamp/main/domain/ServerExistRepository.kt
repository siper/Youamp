package ru.stersh.youamp.main.domain

internal interface ServerExistRepository {
    suspend fun hasServer(): Boolean
}