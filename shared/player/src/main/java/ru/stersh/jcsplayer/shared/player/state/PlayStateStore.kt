package ru.stersh.youamp.shared.player.state

import kotlinx.coroutines.flow.Flow

interface PlayStateStore {
    fun isPlaying(): Flow<Boolean>
}
