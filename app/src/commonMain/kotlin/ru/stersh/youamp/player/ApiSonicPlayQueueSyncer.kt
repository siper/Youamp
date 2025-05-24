package ru.stersh.youamp.player

import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.shared.queue.toMediaItem

class ApiSonicPlayQueueSyncer(
    private val player: Player,
    private val apiProvider: ApiProvider
) {

    suspend fun syncQueue() {
        apiProvider
            .flowApiId()
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest {
                flow<Nothing> {
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
                    .data
                    .playQueue
            }
                .onFailure { Logger.w(it) { "loadPlayQueue error" } }
                .getOrNull()
        } ?: return

        val items = playQueue.entry.map { it.toMediaItem(apiProvider) }
        val currentIndex = items.indexOfFirst { it.id == playQueue.current }
        val position = playQueue.position

        if (currentIndex != -1) {
            player.setMediaItems(items, currentIndex, position ?: 0)
        } else {
            player.setMediaItems(items)
        }
        player.prepare()
    }

    private suspend fun getPlayQueue(player: Player): PlayQueue {
        val items = player
            .getPlayQueue()
            .first()
            .map { it.id }
        val current = player.getCurrentMediaItem().first()?.id
        val position = player.getProgress().first()?.currentTimeMs ?: 0
        return PlayQueue(
            items = items,
            current = current,
            position = position
        )
    }

    private suspend fun syncPlayQueue(player: Player) {
        if (!player.getIsPlaying().first()) {
            return
        }

        val items = player
            .getPlayQueue()
            .first()
            .map { it.id }
        val current = player
            .getCurrentMediaItem()
            .first()
            ?.id
        val position = player
            .getProgress()
            .first()
            ?.currentTimeMs ?: 0

        withContext(Dispatchers.IO) {
            runCatching {
                apiProvider
                    .getApi()
                    .savePlayQueue(items, current, position)
            }.onFailure { Logger.e(it) { "syncPlayQueue error" } }
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
