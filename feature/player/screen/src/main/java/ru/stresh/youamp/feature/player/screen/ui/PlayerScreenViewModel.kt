package ru.stresh.youamp.feature.player.screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.controls.PlayerControls
import ru.stersh.youamp.shared.player.metadata.CurrentSongInfoStore
import ru.stersh.youamp.shared.player.progress.PlayerProgressStore
import ru.stersh.youamp.shared.player.state.PlayStateStore

internal class PlayerScreenViewModel(
    private val currentSongInfoStore: CurrentSongInfoStore,
    private val playStateStore: PlayStateStore,
    private val playerControls: PlayerControls,
    private val playerProgressStore: PlayerProgressStore
) : ViewModel() {
    private val _artworkUrl = MutableStateFlow<String?>(null)
    val artworkUrl: StateFlow<String?>
        get() = _artworkUrl

    private val _title = MutableStateFlow<String?>(null)
    val title: StateFlow<String?>
        get() = _title

    private val _artist = MutableStateFlow<String?>(null)
    val artist: StateFlow<String?>
        get() = _artist

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean>
        get() = _isPlaying

    private val _progress = MutableStateFlow<Float>(0f)
    val progress: StateFlow<Float>
        get() = _progress

    private val _currentTime = MutableStateFlow<String?>(null)
    val currentTime: StateFlow<String?>
        get() = _currentTime

    private val _totalTime = MutableStateFlow<String?>(null)
    val totalTime: StateFlow<String?>
        get() = _totalTime

    init {
        viewModelScope.launch {
            currentSongInfoStore
                .getCurrentSongInfo()
                .filterNotNull()
                .collect { songInfo ->
                    _artworkUrl.value = songInfo.coverArtUrl
                    _title.value = songInfo.title
                    _artist.value = songInfo.artist
                }
        }
        viewModelScope.launch {
            playStateStore
                .isPlaying()
                .collect {
                    _isPlaying.value = it
                }
        }
        viewModelScope.launch {
            playerProgressStore
                .playerProgress()
                .filterNotNull()
                .collect {
                    _progress.value = it.currentTimeMs.toFloat() / it.totalTimeMs
                    _currentTime.value = it.currentTime
                    _totalTime.value = it.totalTime
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
        playerControls.toggleFavorite(isFavorite)
    }

    fun playPause() = viewModelScope.launch {
        if (isPlaying.first()) {
            playerControls.pause()
        } else {
            playerControls.play()
        }
    }
}