package ru.stersh.youamp.feature.player.queue.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.utils.swap
import ru.stersh.youamp.shared.player.controls.PlayerControls
import ru.stersh.youamp.shared.player.queue.PlayerQueueManager
import ru.stersh.youamp.shared.player.state.PlayStateStore

internal class PlayerQueueViewModel(
    private val playerQueueManager: PlayerQueueManager,
    private val playStateStore: PlayStateStore,
    private val playerControls: PlayerControls
) : ViewModel() {

    private val _state = MutableStateFlow(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                playerQueueManager.getQueue(),
                playerQueueManager.currentPlayingItemPosition(),
                playStateStore.isPlaying()
            ) { queue, currentPlayingIndex, isPlaying ->
                return@combine queue.mapIndexed { index, item ->
                    SongUi(
                        id = item.mediaId,
                        title = item.mediaMetadata.title.toString(),
                        artist = item.mediaMetadata.artist.toString(),
                        artworkUrl = item.mediaMetadata.artworkUri?.toString(),
                        isCurrent = index == currentPlayingIndex,
                        isPlaying = index == currentPlayingIndex && isPlaying
                    )
                }
            }
                .collect {
                    _state.value = _state.value.copy(songs = it, progress = false)
                }
        }
    }

    fun playSong(index: Int) = viewModelScope.launch {
        val currentPlayingIndex = playerQueueManager
            .currentPlayingItemPosition()
            .first()
        if (currentPlayingIndex == index) {
            val isPlaying = playStateStore
                .isPlaying()
                .first()

            if (isPlaying) {
                playerControls.pause()
            } else {
                playerControls.play()
            }
        } else {
            playerQueueManager.playPosition(index)
        }
    }

    fun removeSong(index: Int) = viewModelScope.launch {
        playerQueueManager.removeSong(index)
    }

    fun openSongMenu(index: Int) = viewModelScope.launch {
        val currentSong = playerQueueManager
            .getQueue()
            .first()
            .getOrNull(index)
            ?: return@launch

        _state.update {
            it.copy(
                menuSongState = MenuSongStateUi(
                    title = currentSong.mediaMetadata.title?.toString(),
                    artist = currentSong.mediaMetadata.artist?.toString(),
                    artworkUrl = currentSong.mediaMetadata.artworkUri?.toString(),
                    index = index
                )
            )
        }
    }

    fun dismissSongMenu() = viewModelScope.launch {
        _state.update {
            it.copy(menuSongState = null)
        }
    }

    fun moveSong(from: Int, to: Int) = viewModelScope.launch {
        _state.update {
            it.copy(songs = it.songs.swap(from, to))
        }
        playerQueueManager.moveSong(from, to)
    }
}