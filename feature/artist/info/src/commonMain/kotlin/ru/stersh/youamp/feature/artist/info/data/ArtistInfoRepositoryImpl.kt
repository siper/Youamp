package ru.stersh.youamp.feature.artist.info.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.stersh.subsonic.api.SubsonicApi
import ru.stersh.subsonic.api.model.Album
import ru.stersh.subsonic.api.model.Artist
import ru.stersh.youamp.feature.artist.info.domain.ArtistAlbum
import ru.stersh.youamp.feature.artist.info.domain.ArtistInfo
import ru.stersh.youamp.feature.artist.info.domain.ArtistInfoRepository
import ru.stresh.youamp.core.api.ApiProvider
import ru.stresh.youamp.shared.favorites.ArtistFavoritesStorage

internal class ArtistInfoRepositoryImpl(
    private val apiProvider: ApiProvider,
    private val artistFavoritesStorage: ArtistFavoritesStorage
) : ArtistInfoRepository {

    override suspend fun getArtistInfo(id: String): ArtistInfo = coroutineScope {
        val api = apiProvider.getApi()
        val favoriteArtists = async { artistFavoritesStorage.getArtists() }
        val artistInfo = async { api.getArtist(id).data.artist }
        val isFavorite = favoriteArtists
            .await()
            .any { it.id == id }

        return@coroutineScope artistInfo.await().toDomain(api, isFavorite)
    }

    private fun Artist.toDomain(api: SubsonicApi, isFavorite: Boolean): ArtistInfo {
        return ArtistInfo(
            name = name,
            artworkUrl = api.getCoverArtUrl(coverArt),
            isFavorite = isFavorite,
            albums = album
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