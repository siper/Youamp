package ru.stresh.youamp.feature.artists.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.stresh.youamp.core.utils.Paginator
import ru.stresh.youamp.core.utils.data
import ru.stresh.youamp.core.utils.mapItems
import ru.stresh.youamp.core.utils.pageLoader
import ru.stresh.youamp.feature.artists.domain.ArtistsRepository

internal class ArtistsViewModel(private val artistsRepository: ArtistsRepository) : ViewModel() {

    private val paginator = pageLoader { page, pageSize ->
        artistsRepository.getArtists(page, pageSize)
    }

    private val _state = MutableStateFlow<StateUi>(StateUi.Progress)
    val state: StateFlow<StateUi>
        get() = _state

    init {
        viewModelScope.launch {
            combine(
                paginator
                    .data()
                    .mapItems {
                        ArtistUi(
                            id = it.id,
                            name = it.name,
                            albumCount = it.albumCount,
                            artworkUrl = it.artworkUrl
                        )
                    },
                paginator
                    .state()
                    .map { it == Paginator.State.Restart }
            ) { items, isRefreshing ->
                StateUi.Content(isRefreshing, items)
                return@combine StateUi.Content(isRefreshing, items)
            }
                .collect {
                    _state.value = it
                }
        }
    }

    fun loadMore() = viewModelScope.launch {
        paginator.loadNextPage()
    }

    fun refresh() = viewModelScope.launch {
        paginator.restart()
    }

    sealed interface StateUi {
        data class Content(val isRefreshing: Boolean, val items: List<ArtistUi>) : StateUi
        data object Progress : StateUi
    }
}