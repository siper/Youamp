package ru.stresh.youamp.feature.favorite.list.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.core.api.SubsonicApi
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.feature.favorite.list.domain.FavoriteSongsRepository
import ru.stresh.youamp.feature.favorite.list.domain.Favorites
import ru.stresh.youamp.feature.favorite.list.domain.Song

internal class FavoriteSongsRepositoryImpl(private val apiProvider: ApiProvider) : FavoriteSongsRepository {

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

    private fun ru.stersh.youamp.core.api.Song.toDomain(api: SubsonicApi): Song {
        return Song(
            id = id,
            title = title,
            artist = artist,
            artworkUrl = api.getCoverArtUrl(coverArt),
            userRating = userRating
        )
    }
}