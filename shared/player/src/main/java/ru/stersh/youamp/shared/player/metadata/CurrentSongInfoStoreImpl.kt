package ru.stersh.youamp.shared.player.metadata

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.HeartRating
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.StarRating
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.stersh.youamp.shared.player.android.MusicService
import ru.stersh.youamp.shared.player.utils.mediaControllerFuture
import ru.stersh.youamp.shared.player.utils.withPlayer

internal class CurrentSongInfoStoreImpl(private val context: Context) : CurrentSongInfoStore {
    private val executor = ContextCompat.getMainExecutor(context)

    override fun getCurrentSongInfo(): Flow<SongInfo?> = callbackFlow {
        val mediaControllerFuture = mediaControllerFuture(context, MusicService::class.java)

        var player: Player? = null
        var callback: Player.Listener? = null

        mediaControllerFuture.withPlayer(executor) {
            trySend(songInfo)

            val sessionCallback = object : Player.Listener {
                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    trySend(songInfo)
                }
            }
            addListener(sessionCallback)

            player = this
            callback = sessionCallback
        }

        awaitClose {
            callback?.let { player?.removeListener(it) }
            callback = null
            player = null
        }
    }.distinctUntilChanged()

    private val Player.songInfo: SongInfo?
        get() = currentMediaItem?.toSongInfo()

    private fun MediaItem.toSongInfo(): SongInfo {
        return SongInfo(
            id = mediaId,
            title = mediaMetadata.title?.toString(),
            album = mediaMetadata.albumTitle?.toString(),
            artist = mediaMetadata.artist?.toString(),
            coverArtUrl = mediaMetadata.artworkUri?.toString(),
            favorite = (mediaMetadata.userRating as? HeartRating)?.isHeart == true,
            rating = (mediaMetadata.overallRating as? StarRating)?.starRating?.toInt() ?: 0,
        )
    }
}
