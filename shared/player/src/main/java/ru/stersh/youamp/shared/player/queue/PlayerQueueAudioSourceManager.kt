package ru.stersh.youamp.shared.player.queue

import kotlinx.coroutines.flow.Flow

interface PlayerQueueAudioSourceManager {
    fun playingSource(): Flow<PlayingSource?>
    suspend fun playSource(vararg source: AudioSource, shuffled: Boolean = false)
    suspend fun addSource(vararg source: AudioSource, shuffled: Boolean = false)
    suspend fun addAfterCurrent(vararg source: AudioSource, shuffled: Boolean = false)
}
