package ru.stersh.youamp.feature.albums.data

import ru.stersh.youamp.core.api.ListType
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.feature.albums.domain.Album
import ru.stersh.youamp.feature.albums.domain.AlbumsRepository

internal class AlbumsRepositoryImpl(private val apiProvider: ApiProvider) : AlbumsRepository {

    override suspend fun getAlbums(page: Int, pageSize: Int): List<Album> {
        return apiProvider
            .getApi()
            .getAlbumList2(
                type = ListType.ALPHABETICAL_BY_NAME,
                offset = if (page == 1) 0 else (page - 1) * pageSize,
                size = pageSize
            )
            .mapNotNull { album ->
                Album(
                    id = album.id,
                    title = album.name ?: album.album ?: return@mapNotNull null,
                    artist = album.artist,
                    artworkUrl = album.coverArt.let { apiProvider.getApi().getCoverArtUrl(it) }
                )
            }
    }
}