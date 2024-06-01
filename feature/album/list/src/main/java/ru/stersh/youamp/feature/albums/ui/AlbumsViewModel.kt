package ru.stersh.youamp.feature.albums.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.core.ui.AlbumUi
import ru.stersh.youamp.core.utils.Paginator
import ru.stersh.youamp.core.utils.data
import ru.stersh.youamp.core.utils.mapItems
import ru.stersh.youamp.core.utils.pageLoader
import ru.stersh.youamp.feature.albums.domain.AlbumsRepository

internal class AlbumsViewModel(
    private val albumsRepository: AlbumsRepository,
    private val apiProvider: ApiProvider
) : ViewModel() {

    private val paginator = pageLoader { page, pageSize ->
        albumsRepository.getAlbums(page, pageSize)
    }

    private val _state = MutableStateFlow<StateUi>(StateUi.Progress)
    val state: StateFlow<StateUi>
        get() = _state

    init {
        subscribeServerChange()
        subscribeState()
    }

    fun loadMore() = viewModelScope.launch {
        paginator.loadNextPage()
    }

    fun refresh() = viewModelScope.launch {
        paginator.restart()
    }

    private fun subscribeState() {
        viewModelScope.launch {
            combine(
                paginator
                    .data()
                    .mapItems {
                        AlbumUi(
                            id = it.id,
                            title = it.title,
                            artist = it.artist,
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

    private fun subscribeServerChange() {
        viewModelScope.launch {
            apiProvider
                .flowApi()
                .distinctUntilChanged()
                .collect {
                    paginator.restart()
                }
        }
    }
}