package ru.stersh.youamp.feature.artist.data

import ru.stersh.youamp.core.api.Album
import ru.stersh.youamp.core.api.Artist
import ru.stersh.youamp.core.api.SubsonicApi
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.feature.artist.domain.ArtistAlbum
import ru.stersh.youamp.feature.artist.domain.ArtistInfo
import ru.stersh.youamp.feature.artist.domain.ArtistInfoRepository

internal class ArtistInfoRepositoryImpl(
    private val apiProvider: ApiProvider
) : ArtistInfoRepository {

    override suspend fun getArtistInfo(id: String): ArtistInfo {
        val api = apiProvider.getApi()
        val artist = api.getArtist(id)

        return artist.toDomain(api)
    }

    private fun Artist.toDomain(api: SubsonicApi): ArtistInfo {
        return ArtistInfo(
            name = name,
            artworkUrl = api.getCoverArtUrl(coverArt),
            albums = albums
                .orEmpty()
                .map { it.toDomain(api) }
        )
    }

    private fun Album.toDomain(api: SubsonicApi): ArtistAlbum {
        return ArtistAlbum(
            id = id,
            title = requireNotNull(name ?: album),
            artist = null,
            artworkUrl = api.getCoverArtUrl(coverArt)
        )
    }
}