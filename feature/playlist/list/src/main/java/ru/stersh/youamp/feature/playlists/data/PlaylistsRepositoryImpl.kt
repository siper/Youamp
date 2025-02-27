package ru.stersh.youamp.feature.playlists.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.feature.playlists.domain.Playlist
import ru.stersh.youamp.feature.playlists.domain.PlaylistsRepository
import ru.stresh.youamp.core.api.ApiProvider

internal class PlaylistsRepositoryImpl(private val apiProvider: ApiProvider) : PlaylistsRepository {

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return apiProvider
            .flowApi()
            .map { api ->
                api
                    .getPlaylists()
                    .data
                    .playlists
                    .playlist
                    .orEmpty()
                    .map { playlist ->
                        Playlist(
                            id = playlist.id,
                            name = playlist.name,
                            artworkUrl = api.getCoverArtUrl(playlist.coverArt)
                        )
                    }
            }
    }
}