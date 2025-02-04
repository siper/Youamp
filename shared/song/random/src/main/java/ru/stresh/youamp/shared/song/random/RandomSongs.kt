package ru.stresh.youamp.shared.song.random

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

internal class RandomSongs {

    private val needFetch = MutableStateFlow(true)

    private val songs = MutableStateFlow<List<Song>>(emptyList())

    val needFetchData: Boolean
        get() = needFetch.value

    fun songs(): Flow<List<Song>> {
        return needFetch
            .filter { !it }
            .flatMapLatest { songs }
    }

    fun update(
        songs: List<Song>
    ) {
        this.songs.update { songs }
        needFetch.update { false }
    }
}