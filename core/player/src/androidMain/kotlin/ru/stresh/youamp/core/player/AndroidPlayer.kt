package ru.stresh.youamp.core.player

import androidx.media3.common.C
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal class AndroidPlayer(private val player: androidx.media3.common.Player) : Player {

    override suspend fun play() {
        withContext(PlayerDispatcher) {
            player.play()
        }
    }

    override suspend fun pause() {
        withContext(PlayerDispatcher) {
            player.pause()
        }
    }

    override suspend fun next() {
        withContext(PlayerDispatcher) {
            player.seekToNext()
        }
    }

    override suspend fun previous() {
        withContext(PlayerDispatcher) {
            player.seekToPrevious()
        }
    }

    override suspend fun seek(time: Long) {
        withContext(PlayerDispatcher) {
            player.seekTo(time)
        }
    }

    override suspend fun prepare() {
        withContext(PlayerDispatcher) {
            player.prepare()
        }
    }

    override suspend fun seekTo(index: Int, time: Long) {
        withContext(PlayerDispatcher) {
            player.seekTo(index, time)
        }
    }

    override fun getCurrentItemPosition(): Flow<Int?> = callbackFlow {
        val listener = object : androidx.media3.common.Player.Listener {
            override fun onEvents(player: androidx.media3.common.Player, events: androidx.media3.common.Player.Events) {
                if (events.containsAny(
                        androidx.media3.common.Player.EVENT_TIMELINE_CHANGED,
                        androidx.media3.common.Player.EVENT_IS_PLAYING_CHANGED,
                        androidx.media3.common.Player.EVENT_MEDIA_METADATA_CHANGED,
                        androidx.media3.common.Player.EVENT_METADATA,
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
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    override fun getCurrentMediaItem(): Flow<MediaItem?> = callbackFlow {
        val listener = object : androidx.media3.common.Player.Listener {
            override fun onEvents(player: androidx.media3.common.Player, events: androidx.media3.common.Player.Events) {
                if (events.containsAny(androidx.media3.common.Player.EVENT_METADATA, androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION)) {
                    trySend(player.currentMediaItem?.toCommon())
                }
            }
        }

        trySend(player.currentMediaItem?.toCommon())

        player.addListener(listener)

        awaitClose {
            player.removeListener(listener)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    override suspend fun getPlayQueue(): Flow<List<MediaItem>> = callbackFlow<List<MediaItem>> {
        val listener = object : androidx.media3.common.Player.Listener {
            override fun onEvents(player: androidx.media3.common.Player, events: androidx.media3.common.Player.Events) {
                if (events.containsAny(
                        androidx.media3.common.Player.EVENT_TIMELINE_CHANGED,
                        androidx.media3.common.Player.EVENT_IS_PLAYING_CHANGED,
                        androidx.media3.common.Player.EVENT_MEDIA_METADATA_CHANGED,
                        androidx.media3.common.Player.EVENT_METADATA,
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
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    private val androidx.media3.common.Player.mediaItems: List<androidx.media3.common.MediaItem>
        get() = if (mediaItemCount > 0) {
            (0 until mediaItemCount).map { getMediaItemAt(it) }
        } else {
            emptyList()
        }

    override suspend fun setMediaItems(items: List<MediaItem>) = withContext(PlayerDispatcher) {
        player.setMediaItems(
            items.map { it.toPlatform() }
        )
    }

    override suspend fun setMediaItems(items: List<MediaItem>, index: Int, position: Long) = withContext(PlayerDispatcher) {
        player.setMediaItems(items.map { it.toPlatform() }, index, position)
    }

    override suspend fun addMediaItems(items: List<MediaItem>) = withContext(PlayerDispatcher) {
        player.addMediaItems(items.map { it.toPlatform() })
    }

    override suspend fun addMediaItems(index: Int, items: List<MediaItem>) = withContext(PlayerDispatcher) {
        player.addMediaItems(index, items.map { it.toPlatform() })
    }

    override suspend fun clearPlayQueue() = withContext(PlayerDispatcher) {
        player.clearMediaItems()
    }

    override suspend fun playMediaItem(position: Int) = withContext(PlayerDispatcher) {
        player.seekTo(position, 0)
    }

    override suspend fun moveMediaItem(from: Int, to: Int) = withContext(PlayerDispatcher) {
        player.moveMediaItem(from, to)
    }

    override suspend fun removeMediaItem(position: Int) = withContext(PlayerDispatcher) {
        player.removeMediaItem(position)
    }

    override fun getShuffleMode(): Flow<ShuffleMode> = callbackFlow {
        val listener = object : androidx.media3.common.Player.Listener {
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                trySend(player.playerShuffleMode)
            }
        }

        trySend(player.playerShuffleMode)

        player.addListener(listener)

        awaitClose {
            player.removeListener(listener)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    override suspend fun setShuffleMode(shuffleMode: ShuffleMode) {
        withContext(PlayerDispatcher) {
            player.shuffleModeEnabled = shuffleMode == ShuffleMode.Enabled
        }
    }

    private val androidx.media3.common.Player.playerShuffleMode: ShuffleMode
        get() = if (shuffleModeEnabled) {
            ShuffleMode.Enabled
        } else {
            ShuffleMode.Disabled
        }

    override fun getRepeatMode(): Flow<RepeatMode> = callbackFlow {
        val listener = object : androidx.media3.common.Player.Listener {
            override fun onRepeatModeChanged(repeatMode: Int) {
                trySend(player.playerRepeatMode)
            }
        }

        trySend(player.playerRepeatMode)

        player.addListener(listener)

        awaitClose {
            player.removeListener(listener)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    override suspend fun setRepeatMode(repeatMode: RepeatMode) {
        withContext(PlayerDispatcher) {
            player.repeatMode = when (repeatMode) {
                RepeatMode.All -> androidx.media3.common.Player.REPEAT_MODE_ALL
                RepeatMode.One -> androidx.media3.common.Player.REPEAT_MODE_ONE
                RepeatMode.Disabled -> androidx.media3.common.Player.REPEAT_MODE_OFF
            }
        }
    }

    private val androidx.media3.common.Player.playerRepeatMode: RepeatMode
        get() = when (repeatMode) {
            androidx.media3.common.Player.REPEAT_MODE_ALL -> RepeatMode.All
            androidx.media3.common.Player.REPEAT_MODE_ONE -> RepeatMode.One
            else -> RepeatMode.Disabled
        }

    override fun getProgress(): Flow<PlayerProgress?> = channelFlow {
        var progress = getProgress(player)

        trySend(progress)
        while (true) {
            val newProgress = getProgress(player)
            if (newProgress != progress) {
                trySend(newProgress)
                progress = newProgress
            }
            kotlinx.coroutines.delay(PROGRESS_REFRESH_INTERVAL)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    private fun getProgress(player: androidx.media3.common.Player): PlayerProgress? {
        val totalTimeMs = player.duration.takeIf { it != C.TIME_UNSET } ?: return null
        val currentTimeMs = if (player.currentPosition > totalTimeMs) {
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

    override fun getIsPlaying(): Flow<Boolean> = callbackFlow {
        val listener = object : androidx.media3.common.Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                trySend(player.isPlaying)
            }
        }

        player.addListener(listener)

        trySend(player.isPlaying)

        awaitClose {
            player.removeListener(listener)
        }
    }.flowOn(PlayerDispatcher)

    companion object {
        private const val PROGRESS_REFRESH_INTERVAL = 500L
    }
}