package ru.stersh.youamp.shared.player.queue

import kotlinx.coroutines.flow.Flow

interface PlayerQueueAudioSourceManager {
    fun playingSource(): Flow<PlayingSource?>
    suspend fun playSource(source: AudioSource, shuffled: Boolean = false)
    suspend fun addSource(source: AudioSource, shuffled: Boolean = false)
    suspend fun addAfterCurrent(source: AudioSource, shuffled: Boolean = false)
}
