package ru.stersh.youamp

import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.library.MediaLibrary
import ru.stersh.youamp.shared.player.library.MediaLibraryRepository

internal class MediaLibraryRepositoryImpl(
    private val apiProvider: ApiProvider
) : MediaLibraryRepository {

    override suspend fun getPlaylists(): List<MediaLibrary.Playlist> {
        return apiProvider
            .getApi()
            .getPlaylists()
            .map {
                MediaLibrary.Playlist(
                    id = it.id,
                    title = it.name,
                    coverUrl = apiProvider.getApi().getCoverArtUrl(it.coverArt, auth = true)
                )
            }
    }

    override suspend fun getPlaylistSongs(playlistId: String): List<MediaLibrary.Song> {
        val api = apiProvider.getApi()
        return api
            .getPlaylist(playlistId)
            .let { playlist ->
                playlist.entry?.mapNotNull {
                    if (it.isDir || it.isVideo == true) {
                        return@mapNotNull null
                    }
                    MediaLibrary.Song(
                        id = it.id,
                        title = it.title,
                        artist = it.artist,
                        streamUrl = api.streamUrl(it.id),
                        coverUrl = api.getCoverArtUrl(it.coverArt, auth = true)
                    )
                }.orEmpty()
            }
    }

    override suspend fun getSong(songId: String): MediaLibrary.Song {
        val api = apiProvider.getApi()
        return api
            .getSong(songId)
            .let { song ->
                MediaLibrary.Song(
                    id = song.id,
                    title = song.title,
                    artist = song.artist,
                    streamUrl = api.streamUrl(song.id),
                    coverUrl = api.getCoverArtUrl(song.coverArt, auth = true)
                )
            }
    }
}