package ru.stersh.youamp.feature.personal.domain

import kotlinx.coroutines.flow.Flow

internal interface PersonalRepository {
    suspend fun getPersonal(): Flow<Personal>
}