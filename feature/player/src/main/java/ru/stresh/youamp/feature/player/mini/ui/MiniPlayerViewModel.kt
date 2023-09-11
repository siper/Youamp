package ru.stresh.youamp.feature.player.mini.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.controls.PlayerControls
import ru.stersh.youamp.shared.player.metadata.CurrentSongInfoStore
import ru.stersh.youamp.shared.player.progress.PlayerProgressStore
import ru.stersh.youamp.shared.player.state.PlayStateStore

internal class MiniPlayerViewModel(
    private val currentSongInfoStore: CurrentSongInfoStore,
    private val playStateStore: PlayStateStore,
    private val playerControls: PlayerControls,
    private val playerProgressStore: PlayerProgressStore
) : ViewModel() {
    private val _artworkUrl = MutableStateFlow<String?>(null)
    val artworkUrl: Flow<String?>
        get() = _artworkUrl

    private val _title = MutableStateFlow<String?>(null)
    val title: Flow<String?>
        get() = _title

    private val _artist = MutableStateFlow<String?>(null)
    val artist: Flow<String?>
        get() = _artist

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: Flow<Boolean>
        get() = _isPlaying

    private val _progress = MutableStateFlow<Float>(0f)
    val progress: Flow<Float>
        get() = _progress

    private val _isNeedShowPlayer = MutableStateFlow(false)
    val isNeedShowPlayer: Flow<Boolean>
        get() = _isNeedShowPlayer

    init {
        viewModelScope.launch {
            currentSongInfoStore
                .getCurrentSongInfo()
                .collect { songInfo ->
                    if (songInfo == null) {
                        _isNeedShowPlayer.value = false
                        return@collect
                    }
                    _isNeedShowPlayer.value = true
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
                }
        }
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