package ru.stersh.youamp.feature.artist.list.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.subsonic.api.SubsonicApi
import ru.stersh.youamp.feature.artist.list.domain.Artist
import ru.stersh.youamp.feature.artist.list.domain.ArtistsRepository
import ru.stresh.youamp.core.api.ApiProvider

internal class ArtistsRepositoryImpl(private val apiProvider: ApiProvider) : ArtistsRepository {

    override suspend fun getArtists(): Flow<List<Artist>> {
        return apiProvider
            .flowApi()
            .map { api ->
                api
                    .getArtists()
                    .data
                    .artists
                    .index
                    .flatMap { it.artist }
                    .map { artist ->
                        artist.toDomain(api)
                    }
            }

    }

    private fun ru.stersh.subsonic.api.model.Artist.toDomain(api: SubsonicApi): Artist {
        return Artist(
            id = id,
            name = name,
            artworkUrl = api.getCoverArtUrl(coverArt)
        )
    }
}