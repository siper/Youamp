package ru.stersh.youamp.main.domain

import kotlinx.coroutines.flow.Flow

internal interface AvatarUrlRepository {

    fun getAvatarUrl(): Flow<String?>
}