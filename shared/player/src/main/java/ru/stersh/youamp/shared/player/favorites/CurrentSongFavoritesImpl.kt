package ru.stersh.youamp.shared.player.favorites

import androidx.media3.common.HeartRating
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.provider.PlayerProvider
import ru.stersh.youamp.shared.player.utils.PlayerDispatcher
import ru.stersh.youamp.shared.player.utils.PlayerScope

internal class CurrentSongFavoritesImpl(
    private val playerProvider: PlayerProvider,
    private val apiProvider: ApiProvider
) : CurrentSongFavorites {

    override fun toggleFavorite(favorite: Boolean) {
        PlayerScope.launch {
            val player = playerProvider.get()
            val item = player.currentMediaItem ?: return@launch
            val id = item.mediaId
            withContext(Dispatchers.IO) {
                if (favorite) {
                    apiProvider
                        .getApi()
                        .starSong(id)
                } else {
                    apiProvider
                        .getApi()
                        .unstarSong(id)
                }
            }
            toggleFavoriteInMediaItems(player, favorite)
        }
    }

    override fun isFavorite(): Flow<Boolean> = callbackFlow {
        val player = playerProvider.get()
        val listener = object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                trySend(player.isCurrentItemFavorite)
            }
        }
        trySend(player.isCurrentItemFavorite)

        player.addListener(listener)

        awaitClose {
            player.removeListener(listener)
        }
    }
        .flowOn(PlayerDispatcher)
        .distinctUntilChanged()

    private val Player.isCurrentItemFavorite: Boolean
        get() = (currentMediaItem?.mediaMetadata?.userRating as? HeartRating)?.isHeart == true

    private suspend fun toggleFavoriteInMediaItems(player: Player, favorite: Boolean) = withContext(PlayerDispatcher) {
        val item = player.currentMediaItem ?: return@withContext
        val index = player.currentMediaItemIndex.takeIf { it != -1 } ?: return@withContext
        val newMetadata = item
            .mediaMetadata
            .buildUpon()
            .setUserRating(HeartRating(favorite))
            .build()
        val newItem = item
            .buildUpon()
            .setMediaMetadata(newMetadata)
            .build()
        player.replaceMediaItem(index, newItem)
    }
}