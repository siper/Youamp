package ru.stersh.youamp.feature.player.queue.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.core.utils.swap

internal class PlayerQueueViewModel(
    private val player: Player,
) : ViewModel() {
    private val _state = MutableStateFlow(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                player.getPlayQueue(),
                player.getCurrentItemPosition(),
                player.getIsPlaying(),
            ) { queue, currentPlayingIndex, isPlaying ->
                return@combine queue.mapIndexed { index, item ->
                    SongUi(
                        id = item.id,
                        title = item.title,
                        artist = item.artist,
                        artworkUrl = item.artworkUrl,
                        isCurrent = index == currentPlayingIndex,
                        isPlaying = index == currentPlayingIndex && isPlaying,
                    )
                }
            }.map { it.toPersistentList() }
                .flowOn(Dispatchers.IO)
                .collect {
                    _state.value =
                        _state.value.copy(
                            songs = it,
                            progress = false,
                        )
                }
        }
    }

    fun playSong(index: Int) =
        viewModelScope.launch {
            val currentPlayingIndex =
                player
                    .getCurrentItemPosition()
                    .first()
            if (currentPlayingIndex == index) {
                val isPlaying =
                    player
                        .getIsPlaying()
                        .first()

                if (isPlaying) {
                    player.pause()
                } else {
                    player.play()
                }
            } else {
                player.playMediaItem(index)
            }
        }

    fun removeSong(index: Int) =
        viewModelScope.launch {
            player.removeMediaItem(index)
        }

    fun openSongMenu(index: Int) =
        viewModelScope.launch {
            val currentSong =
                player
                    .getPlayQueue()
                    .first()
                    .getOrNull(index)
                    ?: return@launch

            _state.update {
                it.copy(
                    menuSongState =
                        MenuSongStateUi(
                            title = currentSong.title,
                            artist = currentSong.artist,
                            artworkUrl = currentSong.artworkUrl,
                            index = index,
                        ),
                )
            }
        }

    fun dismissSongMenu() =
        viewModelScope.launch {
            _state.update {
                it.copy(menuSongState = null)
            }
        }

    fun moveSong(
        from: Int,
        to: Int,
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                songs =
                    it.songs
                        .swap(
                            from,
                            to,
                        ).toPersistentList(),
            )
        }
        player.moveMediaItem(
            from,
            to,
        )
    }
}
