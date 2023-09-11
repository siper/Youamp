package ru.stresh.youamp.feature.albums.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.stresh.youamp.core.api.ListType
import ru.stresh.youamp.core.api.SubsonicApi
import ru.stresh.youamp.core.api.provider.ApiProvider

internal class AlbumsViewModel(private val apiProvider: ApiProvider) : ViewModel() {

    private var page = 0
    private var pageSize = 10
    private var hasNextPage = true

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing

    private val _albums = MutableStateFlow(emptyList<AlbumUi>())
    val albums: StateFlow<List<AlbumUi>>
        get() = _albums

    init {
        viewModelScope.launch {
            appendNewAlbums()
        }
    }

    fun loadMore() = viewModelScope.launch {
        if (!hasNextPage) {
            return@launch
        }
        page++
        appendNewAlbums()
    }

    fun refresh() = viewModelScope.launch {
        page = 0
        hasNextPage = true
        _isRefreshing.value = true
        val albums = apiProvider.getApi().getAlbumList2(
            ListType.ALPHABETICAL_BY_ARTIST,
            offset = page * pageSize,
            size = pageSize
        )
        _albums.value = albums.mapNotNull { album ->
            AlbumUi(
                id = album.id,
                title = album.name ?: album.album ?: return@mapNotNull null,
                artist = album.artist,
                artworkUrl = album.coverArt.let { apiProvider.getApi().getCoverArtUrl(it) }
            )
        }
        _isRefreshing.value = false
    }

    private suspend fun appendNewAlbums() {
        val albums = apiProvider.getApi().getAlbumList2(
            ListType.ALPHABETICAL_BY_ARTIST,
            offset = page * pageSize,
            size = pageSize
        )
        hasNextPage = albums.isNotEmpty()
        _albums.value += albums.mapNotNull { album ->
            AlbumUi(
                id = album.id,
                title = album.name ?: album.album ?: return@mapNotNull null,
                artist = album.artist,
                artworkUrl = album.coverArt.let { apiProvider.getApi().getCoverArtUrl(it) }
            )
        }
    }
}