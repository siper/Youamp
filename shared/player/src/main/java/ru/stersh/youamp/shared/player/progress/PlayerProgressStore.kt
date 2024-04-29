package ru.stersh.youamp.shared.player.progress

import kotlinx.coroutines.flow.Flow

interface PlayerProgressStore {
    fun playerProgress(): Flow<PlayerProgress?>
}
