package ru.stersh.youamp.shared.song.random

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

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun songs(): Flow<List<Song>> =
        needFetch
            .filter { !it }
            .flatMapLatest { songs }

    fun update(songs: List<Song>) {
        this.songs.update { songs }
        needFetch.update { false }
    }
}
