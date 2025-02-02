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

internal class AlbumInfoRepositoryImpl(
    private val apiProvider: ApiProvider
) : AlbumInfoRepository {

    override suspend fun getAlbumInfo(id: String): AlbumInfo = coroutineScope {
        val api = apiProvider.getApi()
        val starred = async { api.getStarred2() }
        val albumInfo = async { api.getAlbum(id) }
        val isFavorite = starred
            .await()
            .starred2Result
            .album
            ?.any { it.id == id } == true
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