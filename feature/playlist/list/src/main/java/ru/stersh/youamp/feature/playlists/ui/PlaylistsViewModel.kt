package ru.stersh.youamp.feature.playlists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.utils.mapItems
import ru.stersh.youamp.feature.playlists.domain.PlaylistsRepository
import timber.log.Timber

internal class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {
    private var loadJob: Job? = null

    private val _state = MutableStateFlow(PlaylistsStateUi())
    val state: StateFlow<PlaylistsStateUi>
        get() = _state

    init {
        retry()
    }

    fun retry() = viewModelScope.launch {
        _state.update {
            it.copy(
                progress = true,
                isRefreshing = false,
                error = false,
                items = emptyList()
            )
        }
        subscribePlaylists()
    }

    fun refresh() = viewModelScope.launch {
        _state.update {
            it.copy(isRefreshing = true)
        }
        subscribePlaylists()
    }

    private fun subscribePlaylists() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            playlistsRepository
                .getPlaylists()
                .mapItems { it.toUi() }
                .catch { throwable ->
                    Timber.w(throwable)
                    _state.update {
                        it.copy(
                            progress = false,
                            isRefreshing = false,
                            error = true,
                            items = emptyList()
                        )
                    }
                }
                .collect { playlists ->
                    _state.update {
                        it.copy(
                            progress = false,
                            isRefreshing = false,
                            error = false,
                            items = playlists
                        )
                    }
                }
        }
    }

}