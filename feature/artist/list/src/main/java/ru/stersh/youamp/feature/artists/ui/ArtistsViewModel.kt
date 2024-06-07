package ru.stersh.youamp.feature.artists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.utils.mapItems
import ru.stersh.youamp.feature.artists.domain.ArtistsRepository

internal class ArtistsViewModel(private val artistsRepository: ArtistsRepository) : ViewModel() {
    private var artistLoadJob: Job? = null

    private val _state = MutableStateFlow(ArtistsStateUi())
    val state: StateFlow<ArtistsStateUi>
        get() = _state

    init {
        retry()
    }

    fun refresh() {
        _state.update {
            it.copy(isRefreshing = true)
        }
        subscribeArtists()
    }

    fun retry() {
        _state.update {
            it.copy(
                progress = true,
                isRefreshing = false,
                error = false,
                items = emptyList()
            )
        }
        subscribeArtists()
    }

    private fun subscribeArtists() {
        artistLoadJob?.cancel()
        artistLoadJob = viewModelScope.launch {
            artistsRepository
                .getArtists()
                .mapItems { it.toUi() }
                .catch {
                    _state.update {
                        it.copy(
                            progress = false,
                            isRefreshing = false,
                            error = true,
                            items = emptyList()
                        )
                    }
                }
                .collect { artists ->
                    _state.update {
                        it.copy(
                            progress = false,
                            isRefreshing = false,
                            error = false,
                            items = artists
                        )
                    }
                }
        }
    }
}