package ru.stersh.youamp.shared.player.favorites

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.HeartRating
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.android.MusicService
import ru.stersh.youamp.shared.player.utils.MEDIA_SONG_ID
import ru.stersh.youamp.shared.player.utils.mediaControllerFuture
import ru.stersh.youamp.shared.player.utils.withPlayer

internal class CurrentSongFavoritesImpl(
    private val context: Context,
    private val apiProvider: ApiProvider
) : CurrentSongFavorites {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mediaControllerFuture = mediaControllerFuture(context, MusicService::class.java)
    private val executor = ContextCompat.getMainExecutor(context)

    override fun toggleFavorite(favorite: Boolean) {
        mediaControllerFuture.withPlayer(executor) {
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

    override fun isFavorite(): Flow<Boolean> = callbackFlow {
        val mediaControllerFuture = mediaControllerFuture(context, MusicService::class.java)

        var player: Player? = null
        var callback: Player.Listener? = null

        mediaControllerFuture.withPlayer(executor) {
            trySend(isFavorite)

            val sessionCallback = object : Player.Listener {
                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    trySend(isFavorite)
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

    private val Player.isFavorite: Boolean
        get() = (currentMediaItem?.mediaMetadata?.userRating as? HeartRating)?.isHeart == true

    private fun toggleFavoriteInMediaItems(favorite: Boolean) = mediaControllerFuture.withPlayer(executor) {
        val item = currentMediaItem ?: return@withPlayer
        val index = currentMediaItemIndex.takeIf { it != -1 } ?: return@withPlayer
        val newMetadata = item
            .mediaMetadata
            .buildUpon()
            .setUserRating(HeartRating(favorite))
            .build()
        val newItem = item
            .buildUpon()
            .setMediaMetadata(newMetadata)
            .build()
        replaceMediaItem(index, newItem)
    }
}