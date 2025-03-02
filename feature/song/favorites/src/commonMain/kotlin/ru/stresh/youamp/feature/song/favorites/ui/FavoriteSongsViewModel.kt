package ru.stresh.youamp.feature.song.favorites.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stresh.youamp.feature.song.favorites.domain.FavoriteSongsRepository
import ru.stresh.youamp.shared.queue.AudioSource
import ru.stresh.youamp.shared.queue.PlayerQueueAudioSourceManager

internal class FavoriteSongsViewModel(
    private val favoriteSongsRepository: FavoriteSongsRepository,
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
        playFavorites()
    }

    fun playShuffled() = viewModelScope.launch {
        playFavorites(true)
    }

    fun refresh() {
        _state.update {
            it.copy(isRefreshing = true)
        }
        getFavorites()
    }

    fun retry() {
        _state.update {
            it.copy(
                progress = true,
                isRefreshing = false,
                error = false,
                data = null
            )
        }
        getFavorites()
    }

    private suspend fun playFavorites(shuffled: Boolean = false) {
        val favorites = runCatching { favoriteSongsRepository.getFavorites().first() }
            .onFailure { Logger.w(it) { "Filed to load favorites" } }
            .getOrNull()
            ?: return

        val sources = favorites.songs.map {
            AudioSource.RawSong(
                id = it.id,
                title = it.title,
                artist = it.artist,
                album = it.album,
                artworkUrl = it.artworkUrl
            )
        }
        playerQueueAudioSourceManager.playSource(*sources.toTypedArray(), shuffled = shuffled)
    }

    private fun getFavorites() {
        getFavoritesJob?.cancel()
        getFavoritesJob = viewModelScope.launch {
            favoriteSongsRepository
                .getFavorites()
                .map { it.toUi() }
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    Logger.w(throwable) { "Filed to load favorites" }
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