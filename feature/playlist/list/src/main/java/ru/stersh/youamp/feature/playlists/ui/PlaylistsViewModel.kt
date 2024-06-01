package ru.stersh.youamp.feature.playlists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.utils.mapItems
import ru.stersh.youamp.feature.playlists.domain.PlaylistsRepository

internal class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {
    private var loadJob: Job? = null

    private val _state = MutableStateFlow<StateUi>(StateUi.Progress)
    val state: StateFlow<StateUi>
        get() = _state

    init {
        subscribePlaylists()
    }

    fun refresh() = viewModelScope.launch {
        val contentState = _state.value as? StateUi.Content ?: return@launch
        _state.value = contentState.copy(isRefreshing = true)
        subscribePlaylists()
    }

    private fun subscribePlaylists() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            playlistsRepository
                .getPlaylists()
                .mapItems {
                    PlaylistUi(
                        id = it.id,
                        name = it.name,
                        artworkUrl = it.artworkUrl
                    )
                }
                .collect {
                    _state.value = StateUi.Content(false, it)
                }
        }
    }

}