package ru.stresh.youamp.feature.favorite.list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.queue.AudioSource
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.player.queue.PlayerQueueManager
import ru.stresh.youamp.feature.favorite.list.domain.FavoritesRepository
import timber.log.Timber

internal class FavoriteListViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val playerQueueManager: PlayerQueueManager
) : ViewModel() {
    private val _state = MutableStateFlow(FavoriteListStateUi())
    val state: StateFlow<FavoriteListStateUi>
        get() = _state

    private var getFavoritesJob: Job? = null

    init {
        retry()
    }

    fun playAll() = viewModelScope.launch {
        val favorites = runCatching { favoritesRepository.getFavorites().first() }
            .onFailure { Timber.w(it) }
            .getOrNull()
            ?: return@launch

        favorites.songs.forEach {
            playerQueueAudioSourceManager.addSource(
                AudioSource.RawSong(
                    id = it.id,
                    title = it.title,
                    artist = it.artist,
                    artworkUrl = it.artworkUrl,
                    starred = true,
                    userRating = it.userRating
                )
            )
        }
        playerQueueManager.playPosition(0)
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
                favorites = null
            )
        }
        getFavorites()
    }

    private fun getFavorites() {
        getFavoritesJob?.cancel()
        getFavoritesJob = viewModelScope.launch {
            favoritesRepository
                .getFavorites()
                .map { it.toUi() }
                .catch { throwable ->
                    Timber.w(throwable)
                    _state.update {
                        it.copy(
                            progress = false,
                            isRefreshing = false,
                            error = true,
                            favorites = null
                        )
                    }
                }
                .collect { favorites ->
                    _state.update {
                        it.copy(
                            progress = false,
                            isRefreshing = false,
                            error = false,
                            favorites = favorites
                        )
                    }
                }
        }
    }
}