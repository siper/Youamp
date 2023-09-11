package ru.stersh.youamp.shared.player.queue

import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.Flow

interface PlayerQueueManager {
    fun getQueue(): Flow<List<MediaItem>>
    fun currentPlayingItemPosition(): Flow<Int>
    fun clearQueue()
    fun playPosition(position: Int)
    fun moveSong(from: Int, to: Int)
    fun removeSong(position: Int)
}
