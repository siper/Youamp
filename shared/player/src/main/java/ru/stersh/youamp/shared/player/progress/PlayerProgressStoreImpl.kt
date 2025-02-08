package ru.stersh.youamp.shared.player.progress

import androidx.media3.common.C
import androidx.media3.common.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import ru.stersh.youamp.core.utils.formatSongDuration
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerDispatcher

internal class PlayerProgressStoreImpl(
    private val playerProvider: PlayerProvider
) : PlayerProgressStore {

    override fun playerProgress(): Flow<PlayerProgress?> = channelFlow {
        val player = playerProvider.get()

        var progress = getProgress(player)

        trySend(progress)
        while (true) {
            val newProgress = getProgress(player)
            if (newProgress != progress) {
                trySend(newProgress)
                progress = newProgress
            }
            delay(PROGRESS_REFRESH_INTERVAL)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    private fun getProgress(player: Player): PlayerProgress? {
        val totalTimeMs = player.duration.takeIf { it != C.TIME_UNSET } ?: return null
        val currentTimeMs = if (player.currentPosition > totalTimeMs) {
            totalTimeMs
        } else {
            player.currentPosition
        }
        return PlayerProgress(
            totalTimeMs = totalTimeMs,
            currentTimeMs = currentTimeMs,
            totalTime = formatSongDuration(totalTimeMs),
            currentTime = formatSongDuration(currentTimeMs),
        )
    }

    companion object {
        private const val PROGRESS_REFRESH_INTERVAL = 500L
    }
}