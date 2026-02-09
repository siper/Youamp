package ru.stersh.youamp.feature.song.random.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.core.player.ShuffleMode
import ru.stersh.youamp.shared.queue.AudioSource
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.song.random.SongRandomStorage

internal class RandomSongsViewModel(
    private val randomSongsRepository: SongRandomStorage,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val player: Player,
) : ViewModel() {
    private val _state = MutableStateFlow(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    private var getFavoritesJob: Job? = null

    init {
        retry()
    }

    fun playAll() =
        viewModelScope.launch {
            play()
        }

    fun playShuffled() =
        viewModelScope.launch {
            play()
            player.setShuffleMode(ShuffleMode.Enabled)
        }

    fun refresh() =
        viewModelScope.launch {
            _state.update {
                it.copy(isRefreshing = true)
            }
            runCatching { randomSongsRepository.refresh() }.onFailure {
                Logger.w(it) { "Filed to refresh random songs" }
            }
            _state.update {
                it.copy(isRefreshing = false)
            }
        }

    fun retry() =
        viewModelScope.launch {
            _state.update {
                it.copy(
                    progress = true,
                    isRefreshing = false,
                    error = false,
                    data = null,
                )
            }
            runCatching { randomSongsRepository.refresh() }.onFailure {
                Logger.w(it) { "Filed to refresh random songs" }
            }
            getRandom()
        }

    private suspend fun play() {
        val random =
            runCatching { randomSongsRepository.getSongs() }
                .onFailure { Logger.w(it) { "Filed to get random songs" } }
                .getOrNull()
                ?: return

        val sources =
            random.map {
                AudioSource.RawSong(
                    id = it.id,
                    title = it.title,
                    artist = it.artist,
                    album = it.album,
                    artworkUrl = it.artworkUrl,
                )
            }
        playerQueueAudioSourceManager.playSource(*sources.toTypedArray())
    }

    private fun getRandom() {
        getFavoritesJob?.cancel()
        getFavoritesJob =
            viewModelScope.launch {
                randomSongsRepository
                    .flowSongs()
                    .map { it.toUi() }
                    .flowOn(Dispatchers.IO)
                    .catch { throwable ->
                        Logger.w(throwable) { "Filed to flow random songs" }
                        _state.update {
                            it.copy(
                                progress = false,
                                isRefreshing = false,
                                error = true,
                                data = null,
                            )
                        }
                    }.collect { favorites ->
                        _state.update {
                            it.copy(
                                progress = false,
                                isRefreshing = false,
                                error = false,
                                data = favorites,
                            )
                        }
                    }
            }
    }
}
