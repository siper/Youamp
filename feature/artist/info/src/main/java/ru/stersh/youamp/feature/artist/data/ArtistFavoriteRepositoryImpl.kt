package ru.stersh.youamp.feature.artist.data

import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.feature.artist.domain.ArtistFavoriteRepository

internal class ArtistFavoriteRepositoryImpl(
    private val apiProvider: ApiProvider
) : ArtistFavoriteRepository {

    override suspend fun setFavorite(id: String, isFavorite: Boolean) {
        val api = apiProvider.getApi()
        if (isFavorite) {
            api.starArtist(id)
        } else {
            api.unstarArtist(id)
        }
    }
}