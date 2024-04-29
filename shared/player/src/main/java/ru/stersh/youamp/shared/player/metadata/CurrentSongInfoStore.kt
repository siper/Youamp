package ru.stersh.youamp.shared.player.metadata

import kotlinx.coroutines.flow.Flow

interface CurrentSongInfoStore {
    fun getCurrentSongInfo(): Flow<SongInfo?>
}
