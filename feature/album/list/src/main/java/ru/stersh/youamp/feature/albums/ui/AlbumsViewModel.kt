package ru.stersh.youamp.feature.albums.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.core.utils.fold
import ru.stersh.youamp.core.utils.pageLoader
import ru.stersh.youamp.core.utils.result
import ru.stersh.youamp.feature.albums.domain.AlbumsRepository
import timber.log.Timber

internal class AlbumsViewModel(
    private val albumsRepository: AlbumsRepository,
    private val apiProvider: ApiProvider
) : ViewModel() {

    private val paginator = pageLoader { page, pageSize ->
        albumsRepository.getAlbums(page, pageSize)
    }

    private val _state = MutableStateFlow(AlbumsStateUi())
    val state: StateFlow<AlbumsStateUi>
        get() = _state

    init {
        subscribeServerChange()
        subscribeState()
    }

    fun loadMore() = viewModelScope.launch {
        paginator.loadNextPage()
    }

    fun retry() = viewModelScope.launch {
        _state.update {
            it.copy(
                progress = true,
                isRefreshing = false,
                error = false,
                items = persistentListOf()
            )
        }
        paginator.restart()
    }

    fun refresh() = viewModelScope.launch {
        _state.update {
            it.copy(
                isRefreshing = true,
            )
        }
        paginator.restart()
    }

    private fun subscribeState() = viewModelScope.launch {
        paginator
            .result()
            .collect { result ->
                result.fold(
                    onData = { albums ->
                        _state.update {
                            it.copy(
                                progress = false,
                                isRefreshing = false,
                                error = false,
                                items = albums.toUi()
                            )
                        }
                    },
                    onError = { throwable ->
                        Timber.w(throwable)
                        _state.update {
                            it.copy(
                                progress = false,
                                isRefreshing = false,
                                error = true,
                                items = persistentListOf()
                            )
                        }
                    }
                )
            }
    }

    private fun subscribeServerChange() {
        viewModelScope.launch {
            apiProvider
                .flowApi()
                .distinctUntilChanged()
                .collect {
                    _state.update {
                        it.copy(
                            progress = true,
                            isRefreshing = false,
                            error = false,
                            items = persistentListOf()
                        )
                    }
                    paginator.restart()
                }
        }
    }
}