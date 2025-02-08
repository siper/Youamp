package ru.stersh.youamp.player

import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerDispatcher
import ru.stersh.youamp.shared.player.utils.mediaItems
import ru.stersh.youamp.shared.player.utils.toMediaItem
import timber.log.Timber

internal class ApiSonicPlayQueueSyncer(
    private val playerProvider: PlayerProvider,
    private val apiProvider: ApiProvider
) {

    suspend fun syncQueue() {
        apiProvider
            .flowApiId()
            .filterNotNull()
            .flatMapLatest {
                flow<Nothing> {
                    val player = playerProvider.get()
                    loadPlayQueue(player)
                    var playQueue = getPlayQueue(player)
                    while (true) {
                        val currentPlayQueue = getPlayQueue(player)
                        if (currentPlayQueue != playQueue) {
                            syncPlayQueue(player)
                            playQueue = currentPlayQueue
                        }
                        delay(SYNC_QUEUE_PERIOD)
                    }
                }
            }
            .collect()
    }

    private suspend fun loadPlayQueue(player: Player) {
        val playQueue = withContext(Dispatchers.IO) {
            runCatching {
                apiProvider
                    .getApi()
                    .getPlayQueue()
                    .playQueue
            }
                .onFailure { Timber.w(it) }
                .getOrNull()
        } ?: return

        val items = playQueue.entry.map { it.toMediaItem(apiProvider) }
        val currentIndex = items.indexOfFirst { it.mediaId == playQueue.current }
        val position = playQueue.position

        withContext(PlayerDispatcher) {
            if (currentIndex != -1) {
                player.setMediaItems(items, currentIndex, position ?: 0)
            } else {
                player.setMediaItems(items)
            }
            player.prepare()
        }
    }

    private suspend fun getPlayQueue(player: Player) = withContext(PlayerDispatcher) {
        val items = player.mediaItems.map { it.mediaId }
        val current = player.currentMediaItem?.mediaId
        val position = player.currentPosition
        return@withContext PlayQueue(
            items = items,
            current = current,
            position = position
        )
    }

    private suspend fun syncPlayQueue(player: Player) = withContext(PlayerDispatcher) {
        if (!player.isPlaying) {
            return@withContext
        }

        val items = player.mediaItems.map { it.mediaId }
        val current = player.currentMediaItem?.mediaId
        val position = player.currentPosition

        withContext(Dispatchers.IO) {
            runCatching {
                apiProvider
                    .getApi()
                    .savePlayQueue(items, current, position)
            }.onFailure { Timber.w(it) }
        }
    }

    private data class PlayQueue(
        val items: List<String>,
        val current: String?,
        val position: Long
    )

    companion object {
        private const val SYNC_QUEUE_PERIOD = 5000L
    }
}
