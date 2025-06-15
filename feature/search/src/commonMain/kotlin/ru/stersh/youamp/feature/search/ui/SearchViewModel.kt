package ru.stersh.youamp.feature.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.stersh.youamp.feature.search.domain.SearchRepository
import ru.stersh.youamp.shared.queue.AudioSource
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager

internal class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val playerQueueAudioSourceManager: PlayerQueueAudioSourceManager,
) : ViewModel() {
    private val _state = MutableStateFlow(SearchStateUi())
    val state: StateFlow<SearchStateUi>
        get() = _state

    init {
        viewModelScope.launch {
            searchRepository
                .searchResults()
                .flowOn(Dispatchers.IO)
                .collect { searchResult ->
                    val currentState = _state.value
                    _state.value =
                        currentState.copy(
                            progress = false,
                            songs =
                                searchResult
                                    .songs
                                    .map { it.toUi() }
                                    .toPersistentList(),
                            hasMoreSongs = searchResult.hasMoreSongs,
                            songsProgress = false,
                            albums =
                                searchResult
                                    .albums
                                    .map { it.toUi() }
                                    .toPersistentList(),
                            hasMoreAlbums = searchResult.hasMoreAlbums,
                            albumsProgress = false,
                            artists =
                                searchResult
                                    .artists
                                    .map { it.toUi() }
                                    .toPersistentList(),
                            hasMoreArtists = searchResult.hasMoreArtists,
                            artistsProgress = false,
                        )
                }
        }
    }

    fun play(source: AudioSource) =
        viewModelScope.launch {
            playerQueueAudioSourceManager.playSource(source)
        }

    fun addToQueue(source: AudioSource) =
        viewModelScope.launch {
            playerQueueAudioSourceManager.addLast(source)
        }

    fun onQueryChange(query: String) =
        viewModelScope.launch {
            searchRepository.search(query)
        }

    fun onLoadMoreSongs() =
        viewModelScope.launch {
            val currentState = _state.value
            _state.value = currentState.copy(songsProgress = true)
            searchRepository.loadMoreSongs()
        }

    fun onLoadMoreAlbums() =
        viewModelScope.launch {
            val currentState = _state.value
            _state.value = currentState.copy(albumsProgress = true)
            searchRepository.loadMoreAlbums()
        }

    fun onLoadMoreArtists() =
        viewModelScope.launch {
            val currentState = _state.value
            _state.value = currentState.copy(artistsProgress = true)
            searchRepository.loadMoreArtists()
        }
}
