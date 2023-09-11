package ru.stersh.youamp.shared.player.utils

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.Executor

internal inline fun ListenableFuture<MediaController>.withPlayer(executor: Executor, crossinline block: Player.() -> Unit) {
    addListener(
        {
            val player = get()
            block.invoke(player)
        },
        executor,
    )
}

fun mediaControllerFuture(context: Context, clazz: Class<*>): ListenableFuture<MediaController> {
    val sessionToken = SessionToken(context, ComponentName(context, clazz))
    return MediaController
        .Builder(context, sessionToken)
        .buildAsync()
}

internal fun mediaItemsFlow(context: Context, clazz: Class<*>): Flow<List<MediaItem>> = callbackFlow {
    val mediaControllerFuture = mediaControllerFuture(context, clazz)

    var player: Player? = null
    var callback: Player.Listener? = null

    mediaControllerFuture.addListener(
        {
            val sessionPlayer = mediaControllerFuture.get()
            player = sessionPlayer
            var lastItems = sessionPlayer.mediaItems

            trySend(lastItems)

            val sessionCallback = object : Player.Listener {
                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    val newItems = sessionPlayer.mediaItems
                    if (lastItems != newItems) {
                        lastItems = newItems
                        trySend(newItems)
                    }
                }
            }
            callback = sessionCallback
            sessionPlayer.addListener(sessionCallback)
        },
        ContextCompat.getMainExecutor(context),
    )

    awaitClose {
        val c = callback ?: return@awaitClose
        player?.removeListener(c)
    }
}

val Player.mediaItems: List<MediaItem>
    get() = if (mediaItemCount > 0) {
        (0 until mediaItemCount).map { getMediaItemAt(it) }
    } else {
        emptyList()
    }
