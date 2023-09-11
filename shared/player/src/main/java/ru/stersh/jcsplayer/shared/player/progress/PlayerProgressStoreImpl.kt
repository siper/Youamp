package ru.stersh.youamp.shared.player.progress

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.C
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.android.MusicService
import ru.stersh.youamp.shared.player.utils.mediaControllerFuture
import ru.stersh.youamp.shared.player.utils.withPlayer
import ru.stresh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.core.utils.formatSongDuration

internal class PlayerProgressStoreImpl(
    private val context: Context,
    private val apiProvider: ApiProvider,
) : PlayerProgressStore {
    private val executor = ContextCompat.getMainExecutor(context)
    private var scrobbleSender: ScrobbleSender? = null

    override fun playerProgress(): Flow<PlayerProgress?> = callbackFlow {
        val mediaControllerFuture = mediaControllerFuture(context, MusicService::class.java)

        var player: Player? = null
        var callback: Player.Listener? = null

        mediaControllerFuture.withPlayer(executor) {
            trySend(progress)

            launch {
                while (true) {
                    delay(SEND_PROGRESS_DELAY)

                    val id = currentMediaItem?.mediaId
                    if (id != null && scrobbleSender?.id != id) {
                        scrobbleSender = ScrobbleSender(id, apiProvider)
                    }

                    val currentProgress = progress ?: continue
                    when {
                        currentProgress.currentTimeMs > SEND_SCROBBLE_EVENT_TIME -> {
                            launch { scrobbleSender?.trySendScrobble() }
                        }
                        (currentProgress.currentTimeMs + SEND_SCROBBLE_EVENT_TIME) >= currentProgress.totalTimeMs -> {
                            launch { scrobbleSender?.trySendSubmission() }
                        }
                    }
                    trySend(progress)
                }
            }

            val sessionCallback = object : Player.Listener {
                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    trySend(progress)
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

    private val Player.progress: PlayerProgress?
        get() {
            val totalTimeMs = duration.takeIf { it != C.TIME_UNSET } ?: return null
            val currentTimeMs = if (currentPosition > totalTimeMs) {
                totalTimeMs
            } else {
                currentPosition
            }
            return PlayerProgress(
                totalTimeMs = totalTimeMs,
                currentTimeMs = currentTimeMs,
                totalTime = formatSongDuration(totalTimeMs),
                currentTime = formatSongDuration(currentTimeMs),
            )
        }

    companion object {
        private const val SEND_PROGRESS_DELAY = 500L
        private const val SEND_SCROBBLE_EVENT_TIME = 5000L
    }
}
