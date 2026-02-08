package ru.stersh.youamp.audio

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import ru.stersh.youamp.core.player.MediaItem
import ru.stersh.youamp.core.player.PlayerProgress
import ru.stersh.youamp.core.player.RepeatMode
import ru.stersh.youamp.core.player.ShuffleMode
import ru.stersh.youamp.core.player.toCommon
import ru.stersh.youamp.core.player.toPlatform
import ru.stersh.youamp.core.utils.formatSongDuration
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class AndroidPlayer(
    private val context: Context,
) : ru.stersh.youamp.core.player.Player {
    private suspend fun getPlayer(): Player =
        suspendCoroutine { continuation ->
            val sessionToken =
                SessionToken(context, ComponentName(context, MusicService::class.java))
            val controllerFuture =
                MediaController
                    .Builder(context, sessionToken)
                    .buildAsync()
            controllerFuture.addListener(
                {
                    continuation.resume(controllerFuture.get())
                },
                MoreExecutors.directExecutor(),
            )
        }

    override suspend fun play() {
        withContext(Dispatchers.Main.immediate) {
            getPlayer().play()
        }
    }

    override suspend fun pause() {
        withContext(Dispatchers.Main.immediate) {
            getPlayer().pause()
        }
    }

    override suspend fun next() {
        withContext(Dispatchers.Main.immediate) {
            getPlayer().seekToNext()
        }
    }

    override suspend fun previous() {
        withContext(Dispatchers.Main.immediate) {
            getPlayer().seekToPrevious()
        }
    }

    override suspend fun seek(time: Long) {
        withContext(Dispatchers.Main.immediate) {
            getPlayer().seekTo(time)
        }
    }

    override suspend fun prepare() =
        withContext(Dispatchers.Main.immediate) {
            getPlayer().prepare()
        }

    override suspend fun seekTo(
        index: Int,
        time: Long,
    ) {
        withContext(Dispatchers.Main.immediate) {
            getPlayer().seekTo(
                index,
                time,
            )
        }
    }

    override fun getCurrentItemPosition(): Flow<Int?> =
        callbackFlow {
            val player = getPlayer()
            val listener =
                object : Player.Listener {
                    override fun onEvents(
                        player: Player,
                        events: Player.Events,
                    ) {
                        if (events.containsAny(
                                Player.EVENT_TIMELINE_CHANGED,
                                Player.EVENT_IS_PLAYING_CHANGED,
                                Player.EVENT_MEDIA_METADATA_CHANGED,
                                Player.EVENT_METADATA,
                            )
                        ) {
                            trySend(player.currentMediaItemIndex.takeIf { it != -1 })
                        }
                    }
                }

            player.addListener(listener)

            trySend(player.currentMediaItemIndex.takeIf { it != -1 })

            awaitClose {
                player.removeListener(listener)
            }
            trySend(0)
        }.flowOn(Dispatchers.Main.immediate)
            .distinctUntilChanged()

    override fun getCurrentMediaItem(): Flow<MediaItem?> =
        callbackFlow {
            val player = getPlayer()

            val listener =
                object : Player.Listener {
                    override fun onEvents(
                        player: Player,
                        events: Player.Events,
                    ) {
                        if (events.containsAny(
                                Player.EVENT_METADATA,
                                Player.EVENT_MEDIA_ITEM_TRANSITION,
                            )
                        ) {
                            trySend(player.currentMediaItem?.toCommon())
                        }
                    }
                }

            trySend(player.currentMediaItem?.toCommon())

            player.addListener(listener)

            awaitClose {
                player.removeListener(listener)
            }
        }.flowOn(Dispatchers.Main.immediate)
            .distinctUntilChanged()

    override suspend fun getPlayQueue(): Flow<List<MediaItem>> =
        callbackFlow<List<MediaItem>> {
            val player = getPlayer()
            val listener =
                object : Player.Listener {
                    override fun onEvents(
                        player: Player,
                        events: Player.Events,
                    ) {
                        if (events.containsAny(
                                Player.EVENT_TIMELINE_CHANGED,
                                Player.EVENT_IS_PLAYING_CHANGED,
                                Player.EVENT_MEDIA_METADATA_CHANGED,
                                Player.EVENT_METADATA,
                            )
                        ) {
                            trySend(player.mediaItems.map { it.toCommon() })
                        }
                    }
                }
            player.addListener(listener)

            trySend(player.mediaItems.map { it.toCommon() })

            awaitClose {
                player.removeListener(listener)
            }
        }.flowOn(Dispatchers.Main.immediate)
            .distinctUntilChanged()

    private val Player.mediaItems: List<androidx.media3.common.MediaItem>
        get() =
            if (mediaItemCount > 0) {
                (0 until mediaItemCount).map { getMediaItemAt(it) }
            } else {
                emptyList()
            }

    override suspend fun setMediaItems(items: List<MediaItem>) =
        withContext(Dispatchers.Main.immediate) {
            getPlayer().setMediaItems(
                items.map { it.toPlatform() },
            )
        }

    override suspend fun setMediaItems(
        items: List<MediaItem>,
        index: Int,
        position: Long,
    ) = withContext(Dispatchers.Main.immediate) {
        getPlayer().setMediaItems(
            items.map { it.toPlatform() },
            index,
            position,
        )
    }

    override suspend fun addMediaItems(items: List<MediaItem>) =
        withContext(Dispatchers.Main.immediate) {
            getPlayer().addMediaItems(items.map { it.toPlatform() })
        }

    override suspend fun addMediaItems(
        index: Int,
        items: List<MediaItem>,
    ) = withContext(Dispatchers.Main.immediate) {
        getPlayer().addMediaItems(
            index,
            items.map { it.toPlatform() },
        )
    }

    override suspend fun clearPlayQueue() =
        withContext(Dispatchers.Main.immediate) {
            getPlayer().clearMediaItems()
        }

    override suspend fun playMediaItem(position: Int) =
        withContext(Dispatchers.Main.immediate) {
            getPlayer().seekTo(
                position,
                0,
            )
        }

    override suspend fun moveMediaItem(
        from: Int,
        to: Int,
    ) = withContext(Dispatchers.Main.immediate) {
        getPlayer().moveMediaItem(
            from,
            to,
        )
    }

    override suspend fun removeMediaItem(position: Int) =
        withContext(Dispatchers.Main.immediate) {
            getPlayer().removeMediaItem(position)
        }

    override fun getShuffleMode(): Flow<ShuffleMode> =
        callbackFlow {
            val player = getPlayer()
            val listener =
                object : Player.Listener {
                    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                        trySend(player.playerShuffleMode)
                    }
                }

            trySend(player.playerShuffleMode)

            player.addListener(listener)

            trySend(ShuffleMode.Enabled)
            awaitClose {
                player.removeListener(listener)
            }
        }.flowOn(Dispatchers.Main.immediate)
            .distinctUntilChanged()

    override suspend fun setShuffleMode(shuffleMode: ShuffleMode) {
        withContext(Dispatchers.Main.immediate) {
            getPlayer().shuffleModeEnabled = shuffleMode == ShuffleMode.Enabled
        }
    }

    private val Player.playerShuffleMode: ShuffleMode
        get() =
            if (shuffleModeEnabled) {
                ShuffleMode.Enabled
            } else {
                ShuffleMode.Disabled
            }

    override fun getRepeatMode(): Flow<RepeatMode> =
        callbackFlow {
            val player = getPlayer()
            val listener =
                object : Player.Listener {
                    override fun onRepeatModeChanged(repeatMode: Int) {
                        trySend(player.playerRepeatMode)
                    }
                }

            trySend(player.playerRepeatMode)

            player.addListener(listener)

            trySend(RepeatMode.Disabled)
            awaitClose {
                player.removeListener(listener)
            }
        }.flowOn(Dispatchers.Main.immediate)
            .distinctUntilChanged()

    override suspend fun setRepeatMode(repeatMode: RepeatMode) {
        withContext(Dispatchers.Main.immediate) {
            getPlayer().repeatMode =
                when (repeatMode) {
                    RepeatMode.All -> Player.REPEAT_MODE_ALL
                    RepeatMode.One -> Player.REPEAT_MODE_ONE
                    RepeatMode.Disabled -> Player.REPEAT_MODE_OFF
                }
        }
    }

    private val Player.playerRepeatMode: RepeatMode
        get() =
            when (repeatMode) {
                Player.REPEAT_MODE_ALL -> RepeatMode.All
                Player.REPEAT_MODE_ONE -> RepeatMode.One
                else -> RepeatMode.Disabled
            }

    override fun getProgress(): Flow<PlayerProgress?> =
        channelFlow {
            val player = getPlayer()
            var progress = getProgress(player)

            trySend(progress)
            while (true) {
                val newProgress = getProgress(player)
                if (newProgress != progress) {
                    trySend(newProgress)
                    progress = newProgress
                }
                delay(PROGRESS_REFRESH_INTERVAL)
            }
        }.flowOn(Dispatchers.Main.immediate)
            .distinctUntilChanged()

    private fun getProgress(player: Player): PlayerProgress? {
        val totalTimeMs = player.duration.takeIf { it != C.TIME_UNSET } ?: return null
        val currentTimeMs =
            if (player.currentPosition > totalTimeMs) {
                totalTimeMs
            } else {
                player.currentPosition
            }
        return PlayerProgress(
            totalTimeMs = totalTimeMs,
            currentTimeMs = currentTimeMs,
            totalTime = formatSongDuration(totalTimeMs),
            currentTime = formatSongDuration(currentTimeMs),
        )
    }

    override fun getIsPlaying(): Flow<Boolean> =
        callbackFlow {
            val player = getPlayer()
            val listener =
                object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        trySend(player.isPlaying)
                    }
                }

            player.addListener(listener)

            trySend(player.isPlaying)

            awaitClose {
                player.removeListener(listener)
            }
        }.flowOn(Dispatchers.Main.immediate)

    companion object {
        private const val PROGRESS_REFRESH_INTERVAL = 500L
    }
}
