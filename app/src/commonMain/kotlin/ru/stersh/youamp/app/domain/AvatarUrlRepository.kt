package ru.stersh.youamp.app.domain

import kotlinx.coroutines.flow.Flow

internal interface AvatarUrlRepository {
    fun getAvatarUrl(): Flow<String?>
}