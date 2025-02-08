package ru.stersh.youamp.shared.player.mode

import androidx.media3.common.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerDispatcher
import ru.stersh.youamp.shared.player.utils.PlayerScope

internal class PlayerModeStorageImpl(private val playerProvider: PlayerProvider) : PlayerModeStorage {

    override fun getShuffleMode(): Flow<ShuffleMode> = callbackFlow {
        val player = playerProvider.get()

        val listener = object : Player.Listener {
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                trySend(player.playerShuffleMode)
            }
        }

        trySend(player.playerShuffleMode)

        player.addListener(listener)

        awaitClose {
            player.removeListener(listener)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    override fun setShuffleMode(shuffleMode: ShuffleMode) {
        PlayerScope.launch {
            playerProvider.get().shuffleModeEnabled = shuffleMode == ShuffleMode.Enabled
        }
    }

    override fun getRepeatMode(): Flow<RepeatMode> = callbackFlow {
        val player = playerProvider.get()

        val listener = object : Player.Listener {
            override fun onRepeatModeChanged(repeatMode: Int) {
                trySend(player.playerRepeatMode)
            }
        }

        trySend(player.playerRepeatMode)

        player.addListener(listener)

        awaitClose {
            player.removeListener(listener)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    override fun setRepeatMode(repeatMode: RepeatMode) {
        PlayerScope.launch {
            val player = playerProvider.get()
            player.repeatMode = when (repeatMode) {
                RepeatMode.All -> Player.REPEAT_MODE_ALL
                RepeatMode.One -> Player.REPEAT_MODE_ONE
                RepeatMode.Disabled -> Player.REPEAT_MODE_OFF
            }
        }
    }

    private val Player.playerRepeatMode: RepeatMode
        get() = when (repeatMode) {
            Player.REPEAT_MODE_ALL -> RepeatMode.All
            Player.REPEAT_MODE_ONE -> RepeatMode.One
            else -> RepeatMode.Disabled
        }

    private val Player.playerShuffleMode: ShuffleMode
        get() = if (shuffleModeEnabled) {
            ShuffleMode.Enabled
        } else {
            ShuffleMode.Disabled
        }
}