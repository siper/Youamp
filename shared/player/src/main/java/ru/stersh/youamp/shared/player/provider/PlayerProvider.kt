package ru.stersh.youamp.shared.player.provider

import androidx.media3.common.Player

interface PlayerProvider {
    suspend fun get(): Player
}