package ru.stersh.youamp.feature.player.screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.shared.favorites.SongFavoritesStorage

internal class PlayerScreenViewModel(
    private val player: Player,
    private val songFavoritesStorage: SongFavoritesStorage,
) : ViewModel() {
    private val _state = MutableStateFlow(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    init {
        viewModelScope.launch {
            player
                .getCurrentMediaItem()
                .filterNotNull()
                .collect { currentMediaItem ->
                    _state.update {
                        it.copy(
                            artworkUrl = currentMediaItem.artworkUrl,
                            title = currentMediaItem.title,
                            artist = currentMediaItem.artist,
                        )
                    }
                }
        }
        viewModelScope.launch {
            player
                .getIsPlaying()
                .collect { isPlaying ->
                    _state.update {
                        it.copy(
                            isPlaying = isPlaying,
                        )
                    }
                }
        }
        viewModelScope.launch {
            player
                .getProgress()
                .filterNotNull()
                .collect { progress ->
                    _state.update {
                        it.copy(
                            progress = progress.percent,
                            currentTime = progress.currentTime,
                            totalTime = progress.totalTime,
                        )
                    }
                }
        }
        viewModelScope.launch {
            player
                .getRepeatMode()
                .collect { repeatMode ->
                    _state.update {
                        it.copy(
                            repeatMode = repeatMode.toUi(),
                        )
                    }
                }
        }
        viewModelScope.launch {
            player
                .getShuffleMode()
                .collect { shuffleMode ->
                    _state.update {
                        it.copy(
                            shuffleMode = shuffleMode.toUi(),
                        )
                    }
                }
        }
        @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
        viewModelScope.launch {
            player
                .getCurrentMediaItem()
                .map { it?.id }
                .flatMapLatest { currentSongId ->
                    if (currentSongId == null) {
                        flowOf(false)
                    } else {
                        songFavoritesStorage
                            .flowSongs()
                            .map { songs ->
                                songs.any { it.id == currentSongId }
                            }
                    }
                }.flowOn(Dispatchers.IO)
                .collect { isFavorite ->
                    _state.update {
                        it.copy(
                            isFavorite = isFavorite,
                        )
                    }
                }
        }
    }

    fun seekTo(progress: Float) =
        viewModelScope.launch {
            val totalMs =
                player
                    .getProgress()
                    .first()
                    ?.totalTimeMs ?: return@launch
            val time = (totalMs * progress).toLong()
            player.seek(time)
        }

    fun next() =
        viewModelScope.launch {
            player.next()
        }

    fun previous() =
        viewModelScope.launch {
            player.previous()
        }

    fun toggleFavorite(isFavorite: Boolean) =
        viewModelScope.launch {
            val id =
                player
                    .getCurrentMediaItem()
                    .first()
                    ?.id
                    ?: return@launch
            runCatching {
                songFavoritesStorage.setSongFavorite(
                    id,
                    isFavorite,
                )
            }.onFailure { Logger.w(it) { "Filed to like song" } }
        }

    fun playPause() =
        viewModelScope.launch {
            if (_state.value.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }

    fun shuffleModeChanged(shuffleMode: ShuffleModeUi) =
        viewModelScope.launch {
            player.setShuffleMode(shuffleMode.toDomain())
        }

    fun repeatModeChanged(repeatMode: RepeatModeUi) =
        viewModelScope.launch {
            player.setRepeatMode(repeatMode.toDomain())
        }
}
