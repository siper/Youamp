package ru.stersh.youamp.feature.artist.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    override suspend fun getArtistInfo(id: String): ArtistInfo = coroutineScope {
        val api = apiProvider.getApi()
        val starred = async { api.getStarred2() }
        val artistInfo = async { api.getArtist(id) }
        val isFavorite = starred
            .await()
            .starred2Result
            .artist
            ?.any { it.id == id } == true

        return@coroutineScope artistInfo.await().toDomain(api, isFavorite)
    }

    private fun Artist.toDomain(api: SubsonicApi, isFavorite: Boolean): ArtistInfo {
        return ArtistInfo(
            name = name,
            artworkUrl = api.getCoverArtUrl(coverArt),
            isFavorite = isFavorite,
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