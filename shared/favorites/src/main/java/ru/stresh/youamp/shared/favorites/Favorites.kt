package ru.stresh.youamp.shared.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

internal class Favorites {
    private val needFetch = MutableStateFlow(true)

    private val songs = MutableStateFlow<List<Song>>(emptyList())
    private val albums = MutableStateFlow<List<Album>>(emptyList())
    private val artists = MutableStateFlow<List<Artist>>(emptyList())

    val needFetchData: Boolean
        get() = needFetch.value

    fun songs(): Flow<List<Song>> {
        return needFetch
            .filter { !it }
            .flatMapLatest { songs }
    }

    fun albums(): Flow<List<Album>> {
        return needFetch
            .filter { !it }
            .flatMapLatest { albums }
    }

    fun artists(): Flow<List<Artist>> {
        return needFetch
            .filter { !it }
            .flatMapLatest { artists }
    }

    fun update(
        songs: List<Song>,
        albums: List<Album>,
        artists: List<Artist>
    ) {
        this.songs.update { songs }
        this.albums.update { albums }
        this.artists.update { artists }
        needFetch.update { false }
    }
}