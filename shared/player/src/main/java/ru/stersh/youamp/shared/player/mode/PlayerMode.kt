package ru.stersh.youamp.shared.player.mode

import kotlinx.coroutines.flow.Flow

interface PlayerMode {

    fun getShuffleMode(): Flow<ShuffleMode>
    fun setShuffleMode(shuffleMode: ShuffleMode)

    fun getRepeatMode(): Flow<RepeatMode>
    fun setRepeatMode(repeatMode: RepeatMode)
}