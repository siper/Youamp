package ru.stersh.youamp.shared.player.state

import androidx.media3.common.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerDispatcher

internal class PlayStateStoreImpl(
    private val playerProvider: PlayerProvider
) : PlayStateStore {

    override fun isPlaying(): Flow<Boolean> = callbackFlow {

        val player = playerProvider.get()

        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                trySend(player.isPlaying)
            }
        }

        player.addListener(listener)

        trySend(player.isPlaying)

        awaitClose {
            player.removeListener(listener)
        }
    }.flowOn(PlayerDispatcher)
}
