package ru.stersh.youamp.main.domain

import kotlinx.coroutines.flow.Flow

internal interface ServerExistRepository {
    suspend fun hasServer(): Flow<Boolean>
}