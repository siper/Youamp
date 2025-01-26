package ru.stersh.youamp.shared.player.queue

interface PlayerQueueAudioSourceManager {
    suspend fun playSource(vararg source: AudioSource, shuffled: Boolean = false)
    suspend fun addSource(vararg source: AudioSource, shuffled: Boolean = false)
    suspend fun addAfterCurrent(vararg source: AudioSource, shuffled: Boolean = false)
}
