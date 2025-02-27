package ru.stersh.youamp.audio.auto

import ru.stresh.youamp.core.api.ApiProvider


internal class AutoRepositoryImpl(
    private val apiProvider: ApiProvider
) : AutoRepository {

    override suspend fun getPlaylists(): List<Auto.Playlist> {
        return apiProvider
            .getApi()
            .getPlaylists()
            .data
            .playlists
            .playlist
            .orEmpty()
            .map {
                Auto.Playlist(
                    id = it.id,
                    title = it.name,
                    coverUrl = apiProvider
                        .getApi()
                        .getCoverArtUrl(it.coverArt, auth = true)
                )
            }
    }

    override suspend fun getPlaylistSongs(playlistId: String): List<Auto.Song> {
        val api = apiProvider.getApi()
        return api
            .getPlaylist(playlistId)
            .data
            .playlist
            .entry
            ?.mapNotNull {
                if (it.isDir || it.isVideo == true) {
                    return@mapNotNull null
                }
                Auto.Song(
                    id = it.id,
                    title = it.title,
                    artist = it.artist,
                    streamUrl = api.streamUrl(it.id).toString(),
                    coverUrl = api.getCoverArtUrl(it.coverArt, auth = true)
                )
            }
            .orEmpty()
    }

    override suspend fun getSong(songId: String): Auto.Song {
        val api = apiProvider.getApi()
        return api
            .getSong(songId)
            .data
            .song
            .let { song ->
                Auto.Song(
                    id = song.id,
                    title = song.title,
                    artist = song.artist,
                    streamUrl = api.streamUrl(song.id).toString(),
                    coverUrl = api.getCoverArtUrl(song.coverArt, auth = true)
                )
            }
    }
}