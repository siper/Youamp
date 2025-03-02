package ru.stersh.youamp.feature.playlist.info.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import ru.stersh.subsonic.api.model.Playlist
import ru.stersh.youamp.feature.playlist.info.domain.PlaylistInfo
import ru.stersh.youamp.feature.playlist.info.domain.PlaylistInfoRepository
import ru.stersh.youamp.feature.playlist.info.domain.PlaylistSong
import ru.stresh.youamp.core.api.ApiProvider
import ru.stresh.youamp.core.player.Player

internal class PlaylistInfoRepositoryImpl(
    private val apiProvider: ApiProvider,
    private val player: Player
) : PlaylistInfoRepository {

    override fun getPlaylistInfo(playlistId: String): Flow<PlaylistInfo> {
        return flowPlaylist(playlistId).flatMapLatest { playlist ->
            combine(
                player.getCurrentMediaItem(),
                player.getIsPlaying()
            ) { currentMediaItem, isPlaying ->
                val api = apiProvider.getApi()
                return@combine PlaylistInfo(
                    title = playlist.name,
                    artworkUrl = api.getCoverArtUrl(playlist.coverArt),
                    songs = playlist
                        .entry
                        .orEmpty()
                        .map { entry ->
                            val isCurrent = currentMediaItem?.id == entry.id
                            PlaylistSong(
                                id = entry.id,
                                title = entry.title,
                                artist = entry.artist,
                                artworkUrl = api.getCoverArtUrl(entry.coverArt),
                                isCurrent = isCurrent,
                                isPlaying = isCurrent && isPlaying
                            )
                        }
                )
            }
        }
    }

    private fun flowPlaylist(playlistId: String): Flow<Playlist> {
        return flow {
            val api = apiProvider.getApi()
            emit(api.getPlaylist(playlistId).data.playlist)
        }
    }
}