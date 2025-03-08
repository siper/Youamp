package ru.stersh.youamp.shared.song.random

import kotlinx.coroutines.flow.Flow

interface SongRandomStorage {

    fun flowSongs(): Flow<List<Song>>

    suspend fun getSongs(): List<Song>

    suspend fun refresh()
}