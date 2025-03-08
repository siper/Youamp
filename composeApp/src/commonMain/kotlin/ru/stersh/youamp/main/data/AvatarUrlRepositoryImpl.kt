package ru.stersh.youamp.main.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.main.domain.AvatarUrlRepository

internal class AvatarUrlRepositoryImpl(
    private val apiProvider: ApiProvider
) : AvatarUrlRepository {
    override fun getAvatarUrl(): Flow<String?> {
        return apiProvider
            .flowApiOrNull()
            .map { it?.avatarUrl(it.username) }
    }
}