package ru.stersh.youamp.shared.player.metadata

import androidx.media3.common.HeartRating
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.StarRating
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerDispatcher

internal class CurrentSongInfoStoreImpl(private val playerProvider: PlayerProvider) : CurrentSongInfoStore {

    override fun getCurrentSongInfo(): Flow<SongInfo?> = callbackFlow {
        val player = playerProvider.get()
        val listener = object : Player.Listener {

            override fun onEvents(player: Player, events: Player.Events) {
                if (events.containsAny(Player.EVENT_METADATA, Player.EVENT_MEDIA_ITEM_TRANSITION)) {
                    trySend(player.songInfo)
                }
            }
        }

        trySend(player.songInfo)

        player.addListener(listener)

        awaitClose {
            player.removeListener(listener)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    private val Player.songInfo: SongInfo?
        get() = currentMediaItem?.toSongInfo()

    private fun MediaItem.toSongInfo(): SongInfo {
        return SongInfo(
            id = mediaId,
            title = mediaMetadata.title?.toString(),
            album = mediaMetadata.albumTitle?.toString(),
            artist = mediaMetadata.artist?.toString(),
            coverArtUrl = mediaMetadata.artworkUri?.toString(),
            favorite = (mediaMetadata.userRating as? HeartRating)?.isHeart == true,
            rating = (mediaMetadata.overallRating as? StarRating)?.starRating?.toInt() ?: 0,
        )
    }
}
