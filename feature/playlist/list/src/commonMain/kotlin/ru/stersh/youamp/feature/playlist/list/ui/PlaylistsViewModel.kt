package ru.stersh.youamp.feature.playlist.list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.utils.mapItems
import ru.stersh.youamp.feature.playlist.list.domain.PlaylistsRepository

internal class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository,
) : ViewModel() {
    private var loadJob: Job? = null

    private val _state = MutableStateFlow(PlaylistsStateUi())
    val state: StateFlow<PlaylistsStateUi>
        get() = _state

    init {
        retry()
    }

    fun retry() =
        viewModelScope.launch {
            _state.update {
                it.copy(
                    progress = true,
                    isRefreshing = false,
                    error = false,
                    items = persistentListOf(),
                )
            }
            subscribePlaylists()
        }

    fun refresh() =
        viewModelScope.launch {
            _state.update {
                it.copy(isRefreshing = true)
            }
            subscribePlaylists()
        }

    private fun subscribePlaylists() {
        loadJob?.cancel()
        loadJob =
            viewModelScope.launch {
                playlistsRepository
                    .getPlaylists()
                    .mapItems { it.toUi() }
                    .map { it.toPersistentList() }
                    .flowOn(Dispatchers.IO)
                    .catch { throwable ->
                        Logger.w(throwable) { "Filed to load playlists" }
                        _state.update {
                            it.copy(
                                progress = false,
                                isRefreshing = false,
                                error = true,
                                items = persistentListOf(),
                            )
                        }
                    }.collect { playlists ->
                        _state.update {
                            it.copy(
                                progress = false,
                                isRefreshing = false,
                                error = false,
                                items = playlists,
                            )
                        }
                    }
            }
    }
}
