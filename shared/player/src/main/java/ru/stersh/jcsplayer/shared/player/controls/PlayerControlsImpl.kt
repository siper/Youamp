package ru.stersh.youamp.shared.player.controls

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.HeartRating
import androidx.media3.common.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.android.MusicService
import ru.stersh.youamp.shared.player.utils.MEDIA_SONG_ID
import ru.stersh.youamp.shared.player.utils.mediaControllerFuture
import ru.stersh.youamp.shared.player.utils.mediaItems
import ru.stresh.youamp.core.api.provider.ApiProvider

internal class PlayerControlsImpl(
    private val context: Context,
    private val apiProvider: ApiProvider,
) : PlayerControls {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mediaControllerFuture = mediaControllerFuture(context, MusicService::class.java)
    private val mainExecutor = ContextCompat.getMainExecutor(context)

    override fun play() = withPlayer {
        play()
    }

    override fun pause() = withPlayer {
        pause()
    }

    override fun next() = withPlayer {
        seekToNextMediaItem()
    }

    override fun previous() = withPlayer {
        if (currentPosition >= 3000) {
            seekTo(0)
        } else {
            seekToPreviousMediaItem()
        }
    }

    override fun seek(time: Long) = withPlayer {
        seekTo(time)
    }

    override fun toggleFavorite(favorite: Boolean) {
        withPlayer {
            val item = currentMediaItem ?: return@withPlayer
            val id = item.mediaMetadata.extras?.getString(MEDIA_SONG_ID) ?: return@withPlayer
            scope.launch {
                if (favorite) {
                    apiProvider
                        .getApi()
                        .starSong(id)
                } else {
                    apiProvider
                        .getApi()
                        .unstarSong(id)
                }
            }
        }
        toggleFavoriteInMediaItems(favorite)
    }

    private fun toggleFavoriteInMediaItems(favorite: Boolean) = withPlayer {
        val item = currentMediaItem ?: return@withPlayer
        val index = mediaItems.indexOf(item).takeIf { it != -1 } ?: return@withPlayer
        val newMetadata = item
            .mediaMetadata
            .buildUpon()
            .setUserRating(HeartRating(favorite))
            .build()
        val newItem = item
            .buildUpon()
            .setMediaMetadata(newMetadata)
            .build()
        val newMediaItems = mediaItems.toMutableList()
        newMediaItems.removeAt(index)
        newMediaItems.add(index, newItem)
        val startPosition = currentPosition
        setMediaItems(newMediaItems, index, startPosition)
    }

    private inline fun withPlayer(crossinline block: Player.() -> Unit) {
        mediaControllerFuture.addListener(
            {
                val player = mediaControllerFuture.get()
                block.invoke(player)
            },
            mainExecutor,
        )
    }
}
