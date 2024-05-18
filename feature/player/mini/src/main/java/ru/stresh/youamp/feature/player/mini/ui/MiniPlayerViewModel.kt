package ru.stresh.youamp.feature.player.mini.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
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
    private val _state = MutableStateFlow<StateUi>(StateUi.Invisible)
    val state: StateFlow<StateUi>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                currentSongInfoStore.getCurrentSongInfo(),
                playStateStore.isPlaying(),
                playerProgressStore
                    .playerProgress()
                    .mapNotNull { it?.progressPercent }
            ) { songInfo, isPlaying, progress ->
                return@combine if (songInfo == null) {
                    StateUi.Invisible
                } else {
                    StateUi.Content(
                        title = songInfo.title,
                        artist = songInfo.artist,
                        artworkUrl = songInfo.coverArtUrl,
                        isPlaying = isPlaying,
                        progress = progress
                    )
                }
            }
                .collect {
                    _state.value = it
                }
        }
    }

    fun playPause() = viewModelScope.launch {
        if (playStateStore.isPlaying().first()) {
            playerControls.pause()
        } else {
            playerControls.play()
        }
    }
}