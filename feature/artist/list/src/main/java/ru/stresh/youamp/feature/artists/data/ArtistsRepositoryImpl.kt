package ru.stresh.youamp.feature.artists.data

import ru.stresh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.feature.artists.domain.Artist
import ru.stresh.youamp.feature.artists.domain.ArtistsRepository

internal class ArtistsRepositoryImpl(private val apiProvider: ApiProvider) : ArtistsRepository {

    override suspend fun getArtists(page: Int, pageSize: Int): List<Artist> {
        return apiProvider
            .getApi()
            .getArtists()
            .map { artist ->
                Artist(
                    id = artist.id,
                    name = artist.name,
                    albumCount = artist.albumCount,
                    artworkUrl = artist.coverArt.let { apiProvider.getApi().getCoverArtUrl(it) }
                )
            }
    }
}