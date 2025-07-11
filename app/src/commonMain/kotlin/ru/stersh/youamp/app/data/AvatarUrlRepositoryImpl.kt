package ru.stersh.youamp.app.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.app.domain.AvatarUrlRepository
import ru.stersh.youamp.core.api.ApiProvider

internal class AvatarUrlRepositoryImpl(
    private val apiProvider: ApiProvider,
) : AvatarUrlRepository {
    override fun getAvatarUrl(): Flow<String?> =
        apiProvider
            .flowApiOrNull()
            .map { it?.avatarUrl(it.username) }
}
