package ru.stersh.youamp.shared.player.queue

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.media3.common.HeartRating
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.StarRating
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import ru.stersh.youamp.core.api.PlaylistEntry
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.android.MusicService
import ru.stersh.youamp.shared.player.utils.MEDIA_ITEM_ALBUM_ID
import ru.stersh.youamp.shared.player.utils.MEDIA_ITEM_DURATION
import ru.stersh.youamp.shared.player.utils.MEDIA_SONG_ID
import ru.stersh.youamp.shared.player.utils.mediaControllerFuture
import ru.stersh.youamp.shared.player.utils.toMediaItem
import ru.stersh.youamp.shared.player.utils.withPlayer

internal class PlayerQueueAudioSourceManagerImpl(
    context: Context,
    private val apiProvider: ApiProvider,
) : PlayerQueueAudioSourceManager {
    private val mediaController = mediaControllerFuture(context, MusicService::class.java)
    private val mainExecutor = ContextCompat.getMainExecutor(context)

    override suspend fun playSource(source: AudioSource, shuffled: Boolean) {
        val newQueue = getMediaItemsFromSource(source)
        val songId = getPlaySongIdFromSource(source)
        val index = if (songId == null) {
            -1
        } else {
            newQueue.indexOfFirst { it.mediaId == songId }
        }
        mediaController.withPlayer(mainExecutor) {
            if (shuffled) {
                setMediaItems(newQueue.shuffled())
            } else {
                setMediaItems(newQueue)
            }
            if (index != -1) {
                seekTo(index, 0)
            }
            prepare()
            play()
        }
    }

    override suspend fun addSource(source: AudioSource, shuffled: Boolean) {
        val newSongs = getMediaItemsFromSource(source)
        mediaController.withPlayer(mainExecutor) {
            if (shuffled) {
                addMediaItems(newSongs.shuffled())
            } else {
                addMediaItems(newSongs)
            }
        }
    }

    override suspend fun addAfterCurrent(source: AudioSource, shuffled: Boolean) {
        val newSongs = getMediaItemsFromSource(source)
        mediaController.withPlayer(mainExecutor) {
            val index = currentMediaItemIndex + 1
            if (shuffled) {
                addMediaItems(index, newSongs.shuffled())
            } else {
                addMediaItems(index, newSongs)
            }
        }
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

    private suspend fun getMediaItemsFromSource(source: AudioSource): List<MediaItem> {
        return when (source) {
            is AudioSource.Song -> listOf(getSong(source))
            is AudioSource.Album -> getSongs(source)
            is AudioSource.Artist -> getSongs(source)
            is AudioSource.Playlist -> getSongs(source)
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
            .setExtras(
                bundleOf(
                    MEDIA_ITEM_ALBUM_ID to albumId,
                    MEDIA_ITEM_DURATION to (duration ?: 0) * 1000L,
                    MEDIA_SONG_ID to id,
                ),
            )
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
