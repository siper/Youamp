package ru.stresh.youamp.main.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stresh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.main.domain.AvatarUrlRepository

internal class AvatarUrlRepositoryImpl(
    private val apiProvider: ApiProvider
) : AvatarUrlRepository {
    override fun getAvatarUrl(): Flow<String?> {
        return apiProvider
            .flowApi()
            .map { it.avatarUrl(it.username) }
    }
}