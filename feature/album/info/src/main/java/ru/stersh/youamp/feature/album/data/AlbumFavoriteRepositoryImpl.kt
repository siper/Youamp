package ru.stersh.youamp.feature.album.data

import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.feature.album.domain.AlbumFavoriteRepository

internal class AlbumFavoriteRepositoryImpl(
    private val apiProvider: ApiProvider
) : AlbumFavoriteRepository {

    override suspend fun setFavorite(id: String, isFavorite: Boolean) {
        val api = apiProvider.getApi()
        if (isFavorite) {
            api.starAlbum(id)
        } else {
            api.unstarAlbum(id)
        }
    }
}