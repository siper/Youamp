package ru.stresh.youamp.feature.favorite.list.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.stresh.youamp.feature.favorite.list.domain.FavoritesRepository
import timber.log.Timber

internal class FavoriteListViewModel(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FavoriteListStateUi())
    val state: StateFlow<FavoriteListStateUi>
        get() = _state

    private var getFavoritesJob: Job? = null

    init {
        retry()
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