package ru.stresh.youamp.shared.queue

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.stersh.subsonic.api.model.PlaylistEntry
import ru.stresh.youamp.core.api.ApiProvider
import ru.stresh.youamp.core.player.Player

internal class PlayerQueueAudioSourceManagerImpl(
    private val player: Player,
    private val apiProvider: ApiProvider,
) : PlayerQueueAudioSourceManager {
    private val playingSource = MutableStateFlow<PlayingSource?>(null)

    override fun playingSource(): Flow<PlayingSource?> = playingSource

    override suspend fun playSource(vararg source: AudioSource, shuffled: Boolean) = withContext(Dispatchers.IO) {
        val newQueue = source.flatMap { getMediaItemsFromSource(it) }
        val songId = source
            .getOrNull(0)
            ?.let { getPlaySongIdFromSource(it) }
        val index = if (songId == null) {
            -1
        } else {
            newQueue.indexOfFirst { it.id == songId }
        }
        if (shuffled) {
            player.setMediaItems(newQueue.shuffled())
        } else {
            player.setMediaItems(newQueue)
        }
        if (index != -1) {
            player.seekTo(index, 0)
        }
        player.prepare()
        player.play()
        setPlayingSource(source.first())
    }

    override suspend fun addSource(vararg source: AudioSource, shuffled: Boolean) = withContext(Dispatchers.IO) {
        val newSongs = source.flatMap { getMediaItemsFromSource(it) }
        if (shuffled) {
            player.addMediaItems(newSongs.shuffled())
        } else {
            player.addMediaItems(newSongs)
        }
        clearPlayingSource()
    }

    override suspend fun addAfterCurrent(vararg source: AudioSource, shuffled: Boolean) = withContext(Dispatchers.IO) {
        val newSongs = source.flatMap { getMediaItemsFromSource(it) }
        val index = (player
            .getCurrentItemPosition()
            .first() ?: -1) + 1
        if (shuffled) {
            player.addMediaItems(index, newSongs.shuffled())
        } else {
            player.addMediaItems(index, newSongs)
        }
        clearPlayingSource()
    }

    private fun getPlaySongIdFromSource(source: AudioSource): String? {
        if (source is AudioSource.Album) {
            return source.songId
        }
        if (source is AudioSource.Playlist) {
            return source.songId
        }
        return null
    }

    private suspend fun setPlayingSource(source: AudioSource) = withContext(Dispatchers.IO) {
        val serverId = apiProvider.getApiId()
        if (serverId == null) {
            playingSource.update {
                null
            }
            return@withContext
        }
        val type = when (source) {
            is AudioSource.RawSong,
            is AudioSource.Song -> PlayingSource.Type.Song

            is AudioSource.Album -> PlayingSource.Type.Album
            is AudioSource.Artist -> PlayingSource.Type.Artist
            is AudioSource.Playlist -> PlayingSource.Type.Playlist
        }
        playingSource.update {
            PlayingSource(
                serverId = serverId,
                id = source.id,
                type = type
            )
        }
    }

    private fun clearPlayingSource() {
        playingSource.update {
            null
        }
    }

    private suspend fun getMediaItemsFromSource(source: AudioSource): List<ru.stresh.youamp.core.player.MediaItem> =
        withContext(Dispatchers.IO) {
            return@withContext when (source) {
                is AudioSource.Song -> listOf(getSong(source))
                is AudioSource.Album -> getSongs(source)
                is AudioSource.Artist -> getSongs(source)
                is AudioSource.Playlist -> getSongs(source)
                is AudioSource.RawSong -> listOf(getSong(source))
            }
        }

    private suspend fun getSong(source: AudioSource.Song): ru.stresh.youamp.core.player.MediaItem {
        return apiProvider
            .getApi()
            .getSong(source.id)
            .data
            .song
            .toMediaItem(apiProvider)
    }

    private suspend fun getSongs(source: AudioSource.Album): List<ru.stresh.youamp.core.player.MediaItem> {
        val songs = apiProvider
            .getApi()
            .getAlbum(source.id)
            .data
            .album
            .song
            ?.takeIf { it.isNotEmpty() }
            ?: return emptyList()

        return songs.map { song ->
            song.toMediaItem(apiProvider)
        }
    }

    private suspend fun getSongs(source: AudioSource.Artist): List<ru.stresh.youamp.core.player.MediaItem> =
        coroutineScope {
            val albums = apiProvider
                .getApi()
                .getArtist(source.id)
                .data
                .artist
                .album
                ?.takeIf { it.isNotEmpty() }
                ?: return@coroutineScope emptyList()

            return@coroutineScope albums
                .map {
                    async { getSongs(AudioSource.Album(it.id)) }
                }
                .awaitAll()
                .flatten()
        }

    private suspend fun getSongs(source: AudioSource.Playlist): List<ru.stresh.youamp.core.player.MediaItem> =
        coroutineScope {
            return@coroutineScope apiProvider
                .getApi()
                .getPlaylist(source.id)
                .data
                .playlist
                .entry
                ?.map { it.toMediaItem() }
                ?: emptyList()
        }

    private suspend fun getSong(source: AudioSource.RawSong): ru.stresh.youamp.core.player.MediaItem {
        val songUri = apiProvider
            .getApi()
            .streamUrl(source.id)

        return ru.stresh.youamp.core.player.MediaItem(
            id = source.id,
            title = source.title.orEmpty(),
            url = songUri,
            artist = source.artist,
            album = source.album,
            artworkUrl = source.artworkUrl
        )
    }

    private suspend fun PlaylistEntry.toMediaItem(): ru.stresh.youamp.core.player.MediaItem {
        val songUri = apiProvider
            .getApi()
            .streamUrl(id)

        val artworkUri = apiProvider
            .getApi()
            .getCoverArtUrl(coverArt)

        return ru.stresh.youamp.core.player.MediaItem(
            id = id,
            title = title,
            url = songUri,
            artist = artist,
            album = album,
            artworkUrl = artworkUri
        )
    }
}
