package ru.stersh.youamp.feature.playlist.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import ru.stersh.youamp.core.api.Playlist
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.feature.playlist.domain.PlaylistInfo
import ru.stersh.youamp.feature.playlist.domain.PlaylistInfoRepository
import ru.stersh.youamp.feature.playlist.domain.PlaylistSong
import ru.stersh.youamp.shared.player.metadata.CurrentSongInfoStore
import ru.stersh.youamp.shared.player.state.PlayStateStore

internal class PlaylistInfoRepositoryImpl(
    private val playerStateStore: PlayStateStore,
    private val apiProvider: ApiProvider,
    private val currentSongInfoStore: CurrentSongInfoStore,
) : PlaylistInfoRepository {

    override fun getPlaylistInfo(playlistId: String): Flow<PlaylistInfo> {
        return flowPlaylist(playlistId).flatMapLatest { playlist ->
            combine(
                currentSongInfoStore.getCurrentSongInfo(),
                playerStateStore.isPlaying()
            ) { currentSongInfo, isPlaying ->
                val api = apiProvider.getApi()
                return@combine PlaylistInfo(
                    title = playlist.name,
                    artworkUrl = api.getCoverArtUrl(playlist.coverArt),
                    songs = playlist
                        .entry
                        .orEmpty()
                        .map { entry ->
                            val isCurrent = currentSongInfo?.id == entry.id
                            PlaylistSong(
                                id = entry.id,
                                title = entry.title,
                                artist = entry.artist,
                                artworkUrl = entry.coverArt?.let { api.getCoverArtUrl(it) },
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
            emit(api.getPlaylist(playlistId))
        }
    }
}