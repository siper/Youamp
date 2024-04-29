package ru.stresh.youamp.feature.artists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.stresh.youamp.core.utils.mapItems
import ru.stresh.youamp.feature.artists.domain.ArtistsRepository

internal class ArtistsViewModel(private val artistsRepository: ArtistsRepository) : ViewModel() {
    private var artistLoadJob: Job? = null

    private val _state = MutableStateFlow<StateUi>(StateUi.Progress)
    val state: StateFlow<StateUi>
        get() = _state

    init {
        subscribeArtists()
    }

    fun refresh() = viewModelScope.launch {
        val contentState = _state.value as? StateUi.Content ?: return@launch
        _state.value = contentState.copy(isRefreshing = true)
        subscribeArtists()
    }

    private fun subscribeArtists() {
        artistLoadJob?.cancel()
        artistLoadJob = viewModelScope.launch {
            artistsRepository
                .getArtists()
                .mapItems {
                    ArtistUi(
                        id = it.id,
                        name = it.name,
                        albumCount = it.albumCount,
                        artworkUrl = it.artworkUrl
                    )
                }
                .collect {
                    _state.value = StateUi.Content(false, it)
                }
        }
    }
}