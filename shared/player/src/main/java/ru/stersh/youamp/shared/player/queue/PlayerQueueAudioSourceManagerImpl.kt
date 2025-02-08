package ru.stersh.youamp.shared.player.queue

import androidx.core.net.toUri
import androidx.media3.common.HeartRating
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.StarRating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.stersh.youamp.core.api.PlaylistEntry
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerDispatcher
import ru.stersh.youamp.shared.player.utils.toMediaItem

internal class PlayerQueueAudioSourceManagerImpl(
    private val playerProvider: PlayerProvider,
    private val apiProvider: ApiProvider,
) : PlayerQueueAudioSourceManager {
    private val playingSource = MutableStateFlow<PlayingSource?>(null)

    override fun playingSource(): Flow<PlayingSource?> = playingSource

    override suspend fun playSource(vararg source: AudioSource, shuffled: Boolean) = withContext(Dispatchers.IO) {
        val newQueue = source.flatMap { getMediaItemsFromSource(it) }
        val songId = source.getOrNull(0)?.let { getPlaySongIdFromSource(it) }
        val index = if (songId == null) {
            -1
        } else {
            newQueue.indexOfFirst { it.mediaId == songId }
        }
        withContext(PlayerDispatcher) {
            val player = playerProvider.get()
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
        }
        setPlayingSource(source.first())
    }

    override suspend fun addSource(vararg source: AudioSource, shuffled: Boolean) = withContext(Dispatchers.IO) {
        val newSongs = source.flatMap { getMediaItemsFromSource(it) }
        withContext(PlayerDispatcher) {
            val player = playerProvider.get()
            if (shuffled) {
                player.addMediaItems(newSongs.shuffled())
            } else {
                player.addMediaItems(newSongs)
            }
        }
        clearPlayingSource()
    }

    override suspend fun addAfterCurrent(vararg source: AudioSource, shuffled: Boolean) = withContext(Dispatchers.IO) {
        val newSongs = source.flatMap { getMediaItemsFromSource(it) }
        withContext(PlayerDispatcher) {
            val player = playerProvider.get()
            val index = player.currentMediaItemIndex + 1
            if (shuffled) {
                player.addMediaItems(index, newSongs.shuffled())
            } else {
                player.addMediaItems(index, newSongs)
            }
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

    private suspend fun getMediaItemsFromSource(source: AudioSource): List<MediaItem> = withContext(Dispatchers.IO) {
        return@withContext when (source) {
            is AudioSource.Song -> listOf(getSong(source))
            is AudioSource.Album -> getSongs(source)
            is AudioSource.Artist -> getSongs(source)
            is AudioSource.Playlist -> getSongs(source)
            is AudioSource.RawSong -> listOf(getSong(source))
        }
    }

    private suspend fun getSong(source: AudioSource.Song): MediaItem {
        return apiProvider
            .getApi()
            .getSong(source.id)
            .toMediaItem(apiProvider)
    }

    private suspend fun getSongs(source: AudioSource.Album): List<MediaItem> {
        val songs = apiProvider
            .getApi()
            .getAlbum(source.id)
            .song
            ?.takeIf { it.isNotEmpty() }
            ?: return emptyList()

        return songs.map { song ->
            song.toMediaItem(apiProvider)
        }
    }

    private suspend fun getSongs(source: AudioSource.Artist): List<MediaItem> = coroutineScope {
        val albums = apiProvider
            .getApi()
            .getArtist(source.id)
            .albums
            ?.takeIf { it.isNotEmpty() }
            ?: return@coroutineScope emptyList()

        return@coroutineScope albums
            .map {
                async { getSongs(AudioSource.Album(it.id)) }
            }
            .awaitAll()
            .flatten()
    }

    private suspend fun getSongs(source: AudioSource.Playlist): List<MediaItem> = coroutineScope {
        return@coroutineScope apiProvider
            .getApi()
            .getPlaylist(source.id)
            .entry
            ?.map { it.toMediaItem() }
            ?: emptyList()
    }

    private suspend fun getSong(source: AudioSource.RawSong): MediaItem {
        val songUri = apiProvider
            .getApi()
            .downloadUrl(source.id)
            .toUri()

        val starredRating = HeartRating(source.starred != null)

        val songRating = source.userRating
        val rating = if (songRating != null && songRating > 0) {
            StarRating(5, songRating.toFloat())
        } else {
            StarRating(5)
        }

        val metadata = MediaMetadata
            .Builder()
            .setTitle(source.title)
            .setArtist(source.artist)
            .setUserRating(starredRating)
            .setOverallRating(rating)
            .setArtworkUri(source.artworkUrl?.toUri())
            .build()
        val requestMetadata = MediaItem
            .RequestMetadata
            .Builder()
            .setMediaUri(songUri)
            .build()
        return MediaItem
            .Builder()
            .setMediaId(source.id)
            .setMediaMetadata(metadata)
            .setRequestMetadata(requestMetadata)
            .setUri(songUri)
            .build()
    }

    private suspend fun PlaylistEntry.toMediaItem(): MediaItem {
        val songUri = apiProvider
            .getApi()
            .downloadUrl(id)
            .toUri()

        val artworkUri = apiProvider
            .getApi()
            .getCoverArtUrl(coverArt)
            ?.toUri()

        val songRating = userRating
        val rating = if (songRating != null && songRating > 0) {
            StarRating(5, songRating.toFloat())
        } else {
            StarRating(5)
        }
        val starredRating = HeartRating(starred != null)

        val metadata = MediaMetadata
            .Builder()
            .setTitle(title)
            .setArtist(artist)
            .setUserRating(starredRating)
            .setOverallRating(rating)
            .setArtworkUri(artworkUri)
            .build()
        val requestMetadata = MediaItem
            .RequestMetadata
            .Builder()
            .setMediaUri(songUri)
            .build()
        return MediaItem
            .Builder()
            .setMediaId(id)
            .setMediaMetadata(metadata)
            .setRequestMetadata(requestMetadata)
            .setUri(songUri)
            .build()
    }
}
