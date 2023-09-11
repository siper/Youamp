package ru.stersh.youamp.shared.player.queue

interface PlayerQueueAudioSourceManager {
    suspend fun playSource(source: AudioSource, shuffled: Boolean = false)
    suspend fun addSource(source: AudioSource, shuffled: Boolean = false)
    suspend fun addAfterCurrent(source: AudioSource, shuffled: Boolean = false)
}
