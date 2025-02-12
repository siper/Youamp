package ru.stresh.youamp.feature.song.random.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stresh.youamp.shared.song.random.SongRandomStorage
import timber.log.Timber

internal class RandomSongsViewModel(
    private val randomSongsRepository: SongRandomStorage,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager
) : ViewModel() {
    private val _state = MutableStateFlow(StateUi())
    val state: StateFlow<StateUi>
        get() = _state

    private var getFavoritesJob: Job? = null

    init {
        retry()
    }

    fun playAll() = viewModelScope.launch {
        play()
    }

    fun playShuffled() = viewModelScope.launch {
        play(true)
    }

    fun refresh() = viewModelScope.launch {
        _state.update {
            it.copy(isRefreshing = true)
        }
        runCatching { randomSongsRepository.refresh() }.onFailure {
            Timber.w(it)
        }
        _state.update {
            it.copy(isRefreshing = false)
        }
    }

    fun retry() = viewModelScope.launch {
        _state.update {
            it.copy(
                progress = true,
                isRefreshing = false,
                error = false,
                data = null
            )
        }
        runCatching { randomSongsRepository.refresh() }.onFailure {
            Timber.w(it)
        }
        getRandom()
    }

    private suspend fun play(shuffled: Boolean = false) {
        val random = runCatching { randomSongsRepository.getSongs() }
            .onFailure { Timber.w(it) }
            .getOrNull()
            ?: return

        val sources = random.map {
            AudioSource.RawSong(
                id = it.id,
                title = it.title,
                artist = it.artist,
                artworkUrl = it.artworkUrl,
                starred = true,
                userRating = null
            )
        }
        playerQueueAudioSourceManager.playSource(*sources.toTypedArray(), shuffled = shuffled)
    }

    private fun getRandom() {
        getFavoritesJob?.cancel()
        getFavoritesJob = viewModelScope.launch {
            randomSongsRepository
                .flowSongs()
                .map { it.toUi() }
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    Timber.w(throwable)
                    _state.update {
                        it.copy(
                            progress = false,
                            isRefreshing = false,
                            error = true,
                            data = null
                        )
                    }
                }
                .collect { favorites ->
                    _state.update {
                        it.copy(
                            progress = false,
                            isRefreshing = false,
                            error = false,
                            data = favorites
                        )
                    }
                }
        }
    }
}