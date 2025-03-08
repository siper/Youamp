package ru.stersh.youamp.core.player

import kotlinx.coroutines.flow.Flow

interface Player {

    suspend fun play()
    suspend fun pause()
    suspend fun next()
    suspend fun previous()
    suspend fun seek(time: Long)
    suspend fun seekTo(index: Int, time: Long)
    suspend fun prepare()

    fun getCurrentItemPosition(): Flow<Int?>
    fun getCurrentMediaItem(): Flow<MediaItem?>

    suspend fun getPlayQueue(): Flow<List<MediaItem>>
    suspend fun setMediaItems(items: List<MediaItem>)
    suspend fun setMediaItems(items: List<MediaItem>, index: Int, position: Long)
    suspend fun addMediaItems(items: List<MediaItem>)
    suspend fun addMediaItems(index: Int, items: List<MediaItem>)
    suspend fun clearPlayQueue()
    suspend fun playMediaItem(position: Int)
    suspend fun moveMediaItem(from: Int, to: Int)
    suspend fun removeMediaItem(position: Int)

    fun getShuffleMode(): Flow<ShuffleMode>
    suspend fun setShuffleMode(shuffleMode: ShuffleMode)

    fun getRepeatMode(): Flow<RepeatMode>
    suspend fun setRepeatMode(repeatMode: RepeatMode)

    fun getProgress(): Flow<PlayerProgress?>
    fun getIsPlaying(): Flow<Boolean>
}