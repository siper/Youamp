package ru.stresh.youamp.feature.artists.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stresh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.feature.artists.domain.Artist
import ru.stresh.youamp.feature.artists.domain.ArtistsRepository

internal class ArtistsRepositoryImpl(private val apiProvider: ApiProvider) : ArtistsRepository {

    override suspend fun getArtists(): Flow<List<Artist>> {
        return apiProvider
            .flowApi()
            .map {
                it
                    .getArtists()
                    .map { artist ->
                        Artist(
                            id = artist.id,
                            name = artist.name,
                            albumCount = artist.albumCount,
                            artworkUrl = artist.coverArt.let {
                                apiProvider.getApi().getCoverArtUrl(it)
                            }
                        )
                    }
            }

    }
}