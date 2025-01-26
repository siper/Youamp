package ru.stresh.youamp.feature.favorite.list.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.core.api.Song
import ru.stersh.youamp.core.api.SubsonicApi
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.feature.favorite.list.domain.FavoriteSong
import ru.stresh.youamp.feature.favorite.list.domain.Favorites
import ru.stresh.youamp.feature.favorite.list.domain.FavoritesRepository

internal class FavoritesRepositoryImpl(private val apiProvider: ApiProvider) : FavoritesRepository {

    override fun getFavorites(): Flow<Favorites> {
        return apiProvider
            .flowApi()
            .map { api ->
                val starred = api.getStarred2().starred2Result
                Favorites(
                    songs = starred
                        .song
                        .orEmpty()
                        .map { it.toDomain(api) }
                )
            }
    }

    private fun Song.toDomain(api: SubsonicApi): FavoriteSong {
        return FavoriteSong(
            id = id,
            title = title,
            artist = artist,
            artworkUrl = api.getCoverArtUrl(coverArt),
            userRating = userRating
        )
    }
}