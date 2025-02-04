package ru.stersh.youamp.feature.album.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.stersh.youamp.core.api.Album
import ru.stersh.youamp.core.api.Song
import ru.stersh.youamp.core.api.SubsonicApi
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.core.utils.formatSongDuration
import ru.stersh.youamp.feature.album.domain.AlbumInfo
import ru.stersh.youamp.feature.album.domain.AlbumInfoRepository
import ru.stersh.youamp.feature.album.domain.AlbumSong
import ru.stresh.youamp.shared.favorites.AlbumFavoritesStorage

internal class AlbumInfoRepositoryImpl(
    private val apiProvider: ApiProvider,
    private val albumFavoritesStorage: AlbumFavoritesStorage
) : AlbumInfoRepository {

    override suspend fun getAlbumInfo(id: String): AlbumInfo = coroutineScope {
        val api = apiProvider.getApi()
        val favoriteAlbums = async { albumFavoritesStorage.getAlbums() }
        val albumInfo = async { api.getAlbum(id) }
        val isFavorite = favoriteAlbums
            .await()
            .any { it.id == id }
        return@coroutineScope albumInfo
            .await()
            .toDomain(api, isFavorite)
    }

    private fun Album.toDomain(api: SubsonicApi, isFavorite: Boolean): AlbumInfo {
        return AlbumInfo(
            title = requireNotNull(name ?: album),
            artist = artist,
            year = year?.toString(),
            artworkUrl = api.getCoverArtUrl(coverArt),
            isFavorite = isFavorite,
            songs = song
                .orEmpty()
                .map { it.toDomain() }
        )
    }

    private fun Song.toDomain(): AlbumSong {
        return AlbumSong(
            id = id,
            track = track,
            title = title,
            artist = artist,
            duration = formatSongDuration((duration ?: 0) * 1000)
        )
    }
}