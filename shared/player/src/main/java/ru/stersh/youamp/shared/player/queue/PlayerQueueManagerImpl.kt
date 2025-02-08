package ru.stersh.youamp.shared.player.queue

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerDispatcher
import ru.stersh.youamp.shared.player.utils.PlayerScope
import ru.stersh.youamp.shared.player.utils.mediaItems

internal class PlayerQueueManagerImpl(
    private val playerProvider: PlayerProvider
) : PlayerQueueManager {

    override fun currentPlayingItemPosition(): Flow<Int> = callbackFlow {
        val player = playerProvider.get()

        val listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                if (events.containsAny(
                        Player.EVENT_TIMELINE_CHANGED,
                        Player.EVENT_IS_PLAYING_CHANGED,
                        Player.EVENT_MEDIA_METADATA_CHANGED,
                        Player.EVENT_METADATA,
                    )
                ) {
                    trySend(player.currentMediaItemIndex)
                }
            }
        }

        player.addListener(listener)

        trySend(player.currentMediaItemIndex)

        awaitClose {
            player.removeListener(listener)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    override fun getQueue(): Flow<List<MediaItem>> = callbackFlow<List<MediaItem>> {
        val player = playerProvider.get()

        val listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                if (events.containsAny(
                        Player.EVENT_TIMELINE_CHANGED,
                        Player.EVENT_IS_PLAYING_CHANGED,
                        Player.EVENT_MEDIA_METADATA_CHANGED,
                        Player.EVENT_METADATA,
                    )
                ) {
                    trySend(player.mediaItems)
                }
            }
        }
        player.addListener(listener)

        trySend(player.mediaItems)

        awaitClose {
            player.removeListener(listener)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    override fun clearQueue() {
        PlayerScope.launch {
            playerProvider.get().clearMediaItems()
        }
    }

    override fun playPosition(position: Int) {
        PlayerScope.launch {
            val player = playerProvider.get()
            player.seekTo(position, 0L)
            player.play()
        }
    }

    override fun moveSong(from: Int, to: Int) {
        PlayerScope.launch {
            playerProvider
                .get()
                .moveMediaItem(from, to)
        }
    }

    override fun removeSong(position: Int) {
        PlayerScope.launch {
            playerProvider
                .get()
                .removeMediaItem(position)
        }
    }
}
