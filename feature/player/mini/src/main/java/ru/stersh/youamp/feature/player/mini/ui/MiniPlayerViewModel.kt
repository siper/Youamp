package ru.stersh.youamp.feature.player.mini.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stresh.youamp.core.player.Player

internal class MiniPlayerViewModel(
    private val player: Player
) : ViewModel() {
    private val _state = MutableStateFlow<StateUi>(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                player.getCurrentMediaItem(),
                player.getIsPlaying(),
                player
                    .getProgress()
                    .mapNotNull { it?.progressPercent }
            ) { currentMediaItem, isPlaying, progress ->
                return@combine currentMediaItem?.let {
                    PlayerDataUi(
                        title = currentMediaItem.title,
                        artist = currentMediaItem.artist,
                        artworkUrl = currentMediaItem.artworkUrl,
                        isPlaying = isPlaying,
                        progress = progress
                    )
                }
            }
                .collect { data ->
                    _state.update {
                        it.copy(data = data, invisible = data == null)
                    }
                }
        }
    }

    fun playPause() = viewModelScope.launch {
        if (player
                .getIsPlaying()
                .first()
        ) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun next() = viewModelScope.launch {
        player.next()
    }

    fun previous() = viewModelScope.launch {
        player.previous()
    }
}