package ru.stersh.youamp.shared.player.controls

import kotlinx.coroutines.launch
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerScope

internal class PlayerControlsImpl(
    private val playerProvider: PlayerProvider
) : PlayerControls {

    override fun play() {
        PlayerScope.launch {
            playerProvider
                .get()
                .play()
        }
    }

    override fun pause() {
        PlayerScope.launch {
            playerProvider
                .get()
                .pause()
        }
    }

    override fun next() {
        PlayerScope.launch {
            playerProvider
                .get()
                .seekToNextMediaItem()
        }
    }

    override fun previous() {
        PlayerScope.launch {
            val player = playerProvider.get()
            if (player.currentPosition >= 3000) {
                player.seekTo(0)
            } else {
                player.seekToPreviousMediaItem()
            }
        }
    }

    override fun seek(time: Long) {
        PlayerScope.launch {
            playerProvider
                .get()
                .seekTo(time)
        }
    }
}
