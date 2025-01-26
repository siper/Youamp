package ru.stersh.youamp.shared.player.android

import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.utils.mediaItems
import ru.stersh.youamp.shared.player.utils.toMediaItem
import timber.log.Timber

internal class ApiSonicPlayQueueSyncer(private val apiProvider: ApiProvider) {

    suspend fun loadPlayQueue(player: Player) {
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

        if (currentIndex != -1) {
            player.setMediaItems(items, currentIndex, position ?: 0)
        } else {
            player.setMediaItems(items)
        }
        player.prepare()
    }

    suspend fun syncPlayQueue(player: Player) = withContext(Dispatchers.Main) {
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
}
