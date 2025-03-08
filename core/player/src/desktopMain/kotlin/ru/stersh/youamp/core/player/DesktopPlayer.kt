package ru.stersh.youamp.core.player

import javafx.application.Platform
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.stersh.youamp.core.utils.formatSongDuration
import ru.stersh.youamp.core.utils.swap


internal class DesktopPlayer : Player {

    init {
        Platform.startup {}
    }

    private var currentPlayer: MediaPlayer? = null

    private val mutex = Mutex()

    private val playQueue = MutableStateFlow<List<MediaItem>>(emptyList())
    private val currentItemIndex = MutableStateFlow<Int?>(null)
    private val repeatMode = MutableStateFlow(RepeatMode.Disabled)
    private val shuffleMode = MutableStateFlow(ShuffleMode.Disabled)
    private val isPlaying = MutableStateFlow(false)

    private val onPlayingRunnable = Runnable {
        isPlaying.value = true
    }
    private val onStoppedRunnable = Runnable {
        isPlaying.value = false
    }
    private val onEndPlay = Runnable {
        if (repeatMode.value == RepeatMode.One) {
            currentPlayer?.seek(Duration.ZERO)
            currentPlayer?.play()
        } else {
            val currentIndex = currentItemIndex.value
            if (currentIndex != null) {
                val newIndex = currentIndex + 1
                val newItem = playQueue.value.getOrNull(newIndex)
                if (newItem != null) {
                    currentItemIndex.value = newIndex
                    prepareCurrentPlayer()
                    currentPlayer?.play()
                } else {
                    if (repeatMode.value == RepeatMode.All) {
                        currentItemIndex.value = 0
                        prepareCurrentPlayer()
                        currentPlayer?.play()
                    }
                }
            }
        }
    }

    override suspend fun play() {
        mutex.withLock {
            currentPlayer?.play()
        }
    }

    override suspend fun pause() {
        mutex.withLock {
            currentPlayer?.pause()
        }
    }

    override suspend fun next() {
        mutex.withLock {
            val currentIndex = currentItemIndex.value ?: return@withLock
            val nextIndex = currentIndex + 1
            val nextItem = playQueue.value.getOrNull(nextIndex)
            if (nextItem != null) {
                currentItemIndex.value = nextIndex
            } else {
                if (repeatMode.value == RepeatMode.All) {
                    currentItemIndex.value = 0
                } else {
                    return
                }
            }
            prepareCurrentPlayer()
            currentPlayer?.play()
        }
    }

    override suspend fun previous() {
        mutex.withLock {
            val currentIndex = currentItemIndex.value ?: return@withLock
            val nextIndex = currentIndex - 1
            val nextItem = playQueue.value.getOrNull(nextIndex)
            if (nextItem != null) {
                currentItemIndex.value = nextIndex
            } else {
                if (repeatMode.value == RepeatMode.All) {
                    currentItemIndex.value = playQueue.value.lastIndex
                } else {
                    return
                }
            }
            prepareCurrentPlayer()
            currentPlayer?.play()
        }
    }

    override suspend fun seek(time: Long) {
        mutex.withLock {
            currentPlayer?.seek(Duration.millis(time.toDouble()))
        }
    }

    override suspend fun seekTo(index: Int, time: Long) {
        mutex.withLock {
            currentItemIndex.value = index
            prepareCurrentPlayer()
            currentPlayer?.seek(Duration(time.toDouble()))
            if (isPlaying.value) {
                currentPlayer?.play()
            }
        }
    }

    override suspend fun prepare() {}

    override fun getCurrentItemPosition(): Flow<Int?> = currentItemIndex

    override fun getCurrentMediaItem(): Flow<MediaItem?> {
        return currentItemIndex.flatMapLatest { index ->
            if (index == null) {
                flowOf(null)
            } else {
                playQueue.map { it.getOrNull(index) }
            }
        }
    }

    override suspend fun getPlayQueue(): Flow<List<MediaItem>> = playQueue

    override suspend fun setMediaItems(items: List<MediaItem>) {
        mutex.withLock {
            playQueue.value = items
            currentItemIndex.value = if (items.isNotEmpty()) {
                0
            } else {
                null
            }
            prepareCurrentPlayer()
        }
    }

    private fun prepareCurrentPlayer() {
        currentPlayer?.dispose()
        currentPlayer = null
        val currentItemIndex = currentItemIndex.value ?: return
        val currentItem = playQueue.value.getOrNull(currentItemIndex) ?: return
        val media = Media(currentItem.url)
        val newPlayer = MediaPlayer(media)
        newPlayer.onPlaying = onPlayingRunnable
        newPlayer.onPaused = onStoppedRunnable
        newPlayer.onStopped = onStoppedRunnable
        newPlayer.onEndOfMedia = onEndPlay
        currentPlayer = newPlayer
    }

    override suspend fun setMediaItems(items: List<MediaItem>, index: Int, position: Long) {
        mutex.withLock {
            playQueue.value = items
            currentItemIndex.value = if (items.isNotEmpty()) {
                index
            } else {
                null
            }
            prepareCurrentPlayer()
            seek(position)
        }
    }

    override suspend fun addMediaItems(items: List<MediaItem>) {
        mutex.withLock {
            val newItems = playQueue.value + items
            playQueue.value = newItems
            val currentIndex = currentItemIndex.value
            if (currentIndex == null && items.isNotEmpty()) {
                currentItemIndex.value = 0
            }
        }
    }

    override suspend fun addMediaItems(index: Int, items: List<MediaItem>) {
        mutex.withLock {
            val newItems = playQueue.value.toMutableList()
            newItems.addAll(index, items)
            playQueue.value = newItems
            val currentIndex = currentItemIndex.value
            if (currentIndex == null && items.isNotEmpty()) {
                currentItemIndex.value = 0
            }
        }
    }

    override suspend fun clearPlayQueue() {
        mutex.withLock {
            playQueue.value = emptyList()
            currentItemIndex.value = null
            currentPlayer?.dispose()
            currentPlayer = null
        }
    }

    override suspend fun playMediaItem(position: Int) {
        mutex.withLock {
            currentItemIndex.value = position
            prepareCurrentPlayer()
            if (isPlaying.value) {
                currentPlayer?.play()
            }
        }
    }

    override suspend fun moveMediaItem(from: Int, to: Int) {
        mutex.withLock {
            val currentQueue = playQueue.value
            if (currentQueue.isEmpty()) {
                return@withLock
            }
            val currentIndex = currentItemIndex.value ?: return@withLock
            val currentItem = currentQueue.getOrNull(currentIndex) ?: return@withLock
            val newQueue = currentQueue.swap(from, to)
            val newCurrentIndex = newQueue.indexOf(currentItem)
            playQueue.value = newQueue
            currentItemIndex.value = newCurrentIndex
        }
    }

    override suspend fun removeMediaItem(position: Int) {
        mutex.withLock {
            val newPlayQueue = playQueue.value.toMutableList()

            val currentIndex = currentItemIndex.value
            val currentItem = currentIndex?.let { newPlayQueue.getOrNull(it) }
            val deletedItem = newPlayQueue.removeAt(position)

            val newCurrentIndex = when {
                newPlayQueue.isEmpty() -> null
                currentItem != null && currentItem != deletedItem -> newPlayQueue.indexOf(currentItem)
                currentItem != null && currentItem == deletedItem -> {
                    var result: Int? = null
                    for (i in position downTo 0) {
                        if (newPlayQueue.getOrNull(i) != null) {
                            result = i
                            break
                        }
                    }
                    result
                }

                else -> null
            }
            playQueue.value = newPlayQueue.toList()
            currentItemIndex.value = newCurrentIndex
        }
    }

    override fun getShuffleMode(): Flow<ShuffleMode> = shuffleMode

    override suspend fun setShuffleMode(shuffleMode: ShuffleMode) {
        this.shuffleMode.value = shuffleMode
    }

    override fun getRepeatMode(): Flow<RepeatMode> = repeatMode

    override suspend fun setRepeatMode(repeatMode: RepeatMode) {
        this.repeatMode.value = repeatMode
    }

    override fun getProgress(): Flow<PlayerProgress?> {
        return currentItemIndex
            .flatMapLatest {
                if (it == null) {
                    flowOf(null)
                } else {
                    flow {
                        while (true) {
                            val currentPlayer = currentPlayer
                            if (currentPlayer == null) {
                                delay(1000)
                                continue
                            }
                            val currentTimeMs = currentPlayer.currentTime
                                .toMillis()
                                .toLong()
                            val totalTimeMs = currentPlayer.totalDuration
                                .toMillis()
                                .toLong()
                            emit(
                                PlayerProgress(
                                    currentTimeMs = currentTimeMs,
                                    totalTimeMs = totalTimeMs,
                                    currentTime = formatSongDuration(currentTimeMs),
                                    totalTime = formatSongDuration(totalTimeMs)
                                )
                            )
                            delay(1000)
                        }
                    }
                }
            }
    }

    override fun getIsPlaying(): Flow<Boolean> = isPlaying
}