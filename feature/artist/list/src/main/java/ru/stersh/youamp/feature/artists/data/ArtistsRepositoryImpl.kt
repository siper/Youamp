package ru.stersh.youamp.feature.artists.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.core.api.SubsonicApi
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.feature.artists.domain.Artist
import ru.stersh.youamp.feature.artists.domain.ArtistsRepository
import ru.stersh.youamp.core.api.Artist as ApiArtist

internal class ArtistsRepositoryImpl(private val apiProvider: ApiProvider) : ArtistsRepository {

    override suspend fun getArtists(): Flow<List<Artist>> {
        return apiProvider
            .flowApi()
            .map { api ->
                api
                    .getArtists()
                    .map { artist ->
                        artist.toDomain(api)
                    }
            }

    }

    private fun ApiArtist.toDomain(api: SubsonicApi): Artist {
        return Artist(
            id = id,
            name = name,
            artworkUrl = api.getCoverArtUrl(coverArt)
        )
    }
}