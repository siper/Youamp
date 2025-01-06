package ru.stersh.youamp.feature.album.data

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

    override suspend fun getAlbumInfo(id: String): AlbumInfo {
        val api = apiProvider.getApi()
        return api
            .getAlbum(id)
            .toDomain(api)
    }

    private fun Album.toDomain(api: SubsonicApi): AlbumInfo {
        return AlbumInfo(
            title = requireNotNull(name ?: album),
            artist = artist,
            year = year?.toString(),
            artworkUrl = api.getCoverArtUrl(coverArt),
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