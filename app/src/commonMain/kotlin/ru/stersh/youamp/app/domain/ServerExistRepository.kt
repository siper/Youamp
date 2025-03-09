package ru.stersh.youamp.app.domain

import kotlinx.coroutines.flow.Flow

internal interface ServerExistRepository {
    suspend fun hasServer(): Flow<Boolean>
}