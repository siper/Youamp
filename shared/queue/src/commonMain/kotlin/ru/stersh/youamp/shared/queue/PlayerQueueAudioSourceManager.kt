package ru.stersh.youamp.shared.queue

import kotlinx.coroutines.flow.Flow

interface PlayerQueueAudioSourceManager {
    fun playingSource(): Flow<PlayingSource?>

    suspend fun playSource(
        vararg source: AudioSource,
        shuffled: Boolean = false,
    )

    suspend fun addLast(
        vararg source: AudioSource,
        shuffled: Boolean = false,
    )

    suspend fun addNext(
        vararg source: AudioSource,
        shuffled: Boolean = false,
    )
}
