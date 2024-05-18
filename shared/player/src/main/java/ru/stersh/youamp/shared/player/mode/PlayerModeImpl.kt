package ru.stersh.youamp.shared.player.mode

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.stersh.youamp.shared.player.android.MusicService
import ru.stersh.youamp.shared.player.utils.mediaControllerFuture
import ru.stersh.youamp.shared.player.utils.withPlayer

internal class PlayerModeImpl(
    private val context: Context
) : PlayerMode {
    private val mediaControllerFuture = mediaControllerFuture(context, MusicService::class.java)
    private val executor = ContextCompat.getMainExecutor(context)

    override fun getShuffleMode(): Flow<ShuffleMode> = callbackFlow {
        val mediaControllerFuture = mediaControllerFuture(context, MusicService::class.java)

        var player: Player? = null
        var callback: Player.Listener? = null

        mediaControllerFuture.withPlayer(executor) {
            trySend(playerShuffleMode)

            val sessionCallback = object : Player.Listener {
                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                    trySend(playerShuffleMode)
                }
            }
            addListener(sessionCallback)

            player = this
            callback = sessionCallback
        }

        awaitClose {
            callback?.let { player?.removeListener(it) }
            callback = null
            player = null
        }
    }.distinctUntilChanged()

    override fun setShuffleMode(shuffleMode: ShuffleMode) = mediaControllerFuture.withPlayer(executor) {
        this.shuffleModeEnabled = shuffleMode == ShuffleMode.Enabled
    }

    override fun getRepeatMode(): Flow<RepeatMode> = callbackFlow {
        val mediaControllerFuture = mediaControllerFuture(context, MusicService::class.java)

        var player: Player? = null
        var callback: Player.Listener? = null

        mediaControllerFuture.withPlayer(executor) {
            trySend(playerRepeatMode)

            val sessionCallback = object : Player.Listener {
                override fun onRepeatModeChanged(repeatMode: Int) {
                    trySend(playerRepeatMode)
                }
            }
            addListener(sessionCallback)

            player = this
            callback = sessionCallback
        }

        awaitClose {
            callback?.let { player?.removeListener(it) }
            callback = null
            player = null
        }
    }.distinctUntilChanged()

    override fun setRepeatMode(repeatMode: RepeatMode) = mediaControllerFuture.withPlayer(executor) {
        this.repeatMode = when (repeatMode) {
            RepeatMode.All -> Player.REPEAT_MODE_ALL
            RepeatMode.One -> Player.REPEAT_MODE_ONE
            RepeatMode.Disabled -> Player.REPEAT_MODE_OFF
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