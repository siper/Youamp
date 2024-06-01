package ru.stersh.youamp.feature.playlists.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.feature.playlists.domain.Playlist
import ru.stersh.youamp.feature.playlists.domain.PlaylistsRepository

internal class PlaylistsRepositoryImpl(private val apiProvider: ApiProvider) : PlaylistsRepository {

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return apiProvider
            .flowApi()
            .map { api ->
                api
                    .getPlaylists()
                    .map { artist ->
                        Playlist(
                            id = artist.id,
                            name = artist.name,
                            artworkUrl = artist.coverArt.let {
                                apiProvider.getApi().getCoverArtUrl(it)
                            }
                        )
                    }
            }
    }
}