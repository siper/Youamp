package ru.stersh.youamp.shared.player.controls

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import ru.stersh.youamp.shared.player.android.MusicService
import ru.stersh.youamp.shared.player.utils.mediaControllerFuture

internal class PlayerControlsImpl(
    private val context: Context
) : PlayerControls {
    private val mediaControllerFuture = mediaControllerFuture(context, MusicService::class.java)
    private val mainExecutor = ContextCompat.getMainExecutor(context)

    override fun play() = withPlayer {
        play()
    }

    override fun pause() = withPlayer {
        pause()
    }

    override fun next() = withPlayer {
        seekToNextMediaItem()
    }

    override fun previous() = withPlayer {
        if (currentPosition >= 3000) {
            seekTo(0)
        } else {
            seekToPreviousMediaItem()
        }
    }

    override fun seek(time: Long) = withPlayer {
        seekTo(time)
    }

    private inline fun withPlayer(crossinline block: Player.() -> Unit) {
        mediaControllerFuture.addListener(
            {
                val player = mediaControllerFuture.get()
                block.invoke(player)
            },
            mainExecutor,
        )
    }
}
