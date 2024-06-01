package ru.stersh.youamp.feature.player.screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.controls.PlayerControls
import ru.stersh.youamp.shared.player.favorites.CurrentSongFavorites
import ru.stersh.youamp.shared.player.metadata.CurrentSongInfoStore
import ru.stersh.youamp.shared.player.mode.PlayerMode
import ru.stersh.youamp.shared.player.progress.PlayerProgressStore
import ru.stersh.youamp.shared.player.state.PlayStateStore

internal class PlayerScreenViewModel(
    private val currentSongInfoStore: CurrentSongInfoStore,
    private val playStateStore: PlayStateStore,
    private val playerControls: PlayerControls,
    private val playerProgressStore: PlayerProgressStore,
    private val currentSongFavorites: CurrentSongFavorites,
    private val playerMode: PlayerMode
) : ViewModel() {

    private val _state = MutableStateFlow(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    init {
        viewModelScope.launch {
            currentSongInfoStore
                .getCurrentSongInfo()
                .filterNotNull()
                .collect { songInfo ->
                    _state.update {
                        it.copy(
                            artworkUrl = songInfo.coverArtUrl,
                            title = songInfo.title,
                            artist = songInfo.artist
                        )
                    }
                }
        }
        viewModelScope.launch {
            playStateStore
                .isPlaying()
                .collect { isPlaying ->
                    _state.update {
                        it.copy(
                            isPlaying = isPlaying
                        )
                    }
                }
        }
        viewModelScope.launch {
            playerProgressStore
                .playerProgress()
                .filterNotNull()
                .collect { progress ->
                    _state.update {
                        it.copy(
                            progress = progress.currentTimeMs.toFloat() / progress.totalTimeMs,
                            currentTime = progress.currentTime,
                            totalTime = progress.totalTime
                        )
                    }
                }
        }
        viewModelScope.launch {
            playerMode
                .getRepeatMode()
                .collect { repeatMode ->
                    _state.update {
                        it.copy(
                            repeatMode = repeatMode.toUi()
                        )
                    }
                }
        }
        viewModelScope.launch {
            playerMode
                .getShuffleMode()
                .collect { shuffleMode ->
                    _state.update {
                        it.copy(
                            shuffleMode = shuffleMode.toUi()
                        )
                    }
                }
        }
        viewModelScope.launch {
            currentSongFavorites
                .isFavorite()
                .collect { isFavorite ->
                    _state.update {
                        it.copy(
                            isFavorite = isFavorite
                        )
                    }
                }
        }
    }

    fun seekTo(progress: Float) = viewModelScope.launch {
        val totalMs = playerProgressStore
            .playerProgress()
            .first()
            ?.totalTimeMs ?: return@launch
        val time = (totalMs * progress).toLong()
        playerControls.seek(time)
    }

    fun next() = viewModelScope.launch {
        playerControls.next()
    }

    fun previous() = viewModelScope.launch {
        playerControls.previous()
    }

    fun toggleFavorite(isFavorite: Boolean) = viewModelScope.launch {
        currentSongFavorites.toggleFavorite(isFavorite)
    }

    fun playPause() = viewModelScope.launch {
        if (_state.value.isPlaying) {
            playerControls.pause()
        } else {
            playerControls.play()
        }
    }

    fun shuffleModeChanged(shuffleMode: ShuffleModeUi) = viewModelScope.launch {
        playerMode.setShuffleMode(shuffleMode.toDomain())
    }

    fun repeatModeChanged(repeatMode: RepeatModeUi) = viewModelScope.launch {
        playerMode.setRepeatMode(repeatMode.toDomain())
    }
}