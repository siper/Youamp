package ru.stersh.youamp.feature.album.favorites.ui

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
import ru.stersh.youamp.feature.album.favorites.domain.FavoriteAlbumsRepository
import ru.stersh.youamp.shared.queue.AudioSource
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager

internal class FavoriteAlbumsViewModel(
    private val favoriteAlbumsRepository: FavoriteAlbumsRepository,
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
        val favorites = runCatching { favoriteAlbumsRepository.getFavorites().first() }
            .onFailure { Logger.w(it) { "Error loading favorites for play" } }
            .getOrNull()
            ?: return

        val sources = favorites.albums.map {
            AudioSource.Album(id = it.id)
        }
        playerQueueAudioSourceManager.playSource(*sources.toTypedArray(), shuffled = shuffled)
    }

    private fun getFavorites() {
        getFavoritesJob?.cancel()
        getFavoritesJob = viewModelScope.launch {
            favoriteAlbumsRepository
                .getFavorites()
                .map { it.toUi() }
                .flowOn(Dispatchers.IO)
                .catch { throwable ->
                    Logger.w(throwable) { "Error loading favorites" }
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