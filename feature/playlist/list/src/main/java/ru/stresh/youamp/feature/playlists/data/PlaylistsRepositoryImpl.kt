package ru.stresh.youamp.feature.playlists.data

import ru.stresh.youamp.core.api.provider.ApiProvider
import ru.stresh.youamp.feature.playlists.domain.Playlist
import ru.stresh.youamp.feature.playlists.domain.PlaylistsRepository

internal class PlaylistsRepositoryImpl(private val apiProvider: ApiProvider) : PlaylistsRepository {

    override suspend fun getPlaylists(page: Int, pageSize: Int): List<Playlist> {
        return apiProvider
            .getApi()
            .getPlaylists()
            .map { artist ->
                Playlist(
                    id = artist.id,
                    name = artist.name,
                    artworkUrl = artist.coverArt.let { apiProvider.getApi().getCoverArtUrl(it) }
                )
            }
    }
}