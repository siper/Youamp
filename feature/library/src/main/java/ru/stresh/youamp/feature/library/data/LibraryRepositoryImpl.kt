package ru.stresh.youamp.feature.library.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.core.api.Album
import ru.stersh.youamp.core.api.Artist
import ru.stersh.youamp.core.api.ListType
import ru.stersh.youamp.core.api.SubsonicApi
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.player.queue.PlayingSource
import ru.stersh.youamp.shared.player.state.PlayStateStore
import ru.stresh.youamp.feature.library.domain.Library
import ru.stresh.youamp.feature.library.domain.LibraryRepository

internal class LibraryRepositoryImpl(
    private val apiProvider: ApiProvider,
    private val queueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val playStateStore: PlayStateStore
) : LibraryRepository {

    override fun getLibrary(): Flow<Library> {
        return combine(
            apiProvider
                .flowApi()
                .map { api ->
                    ApiLibrary(
                        albums = api.getAlbumList2(
                            type = ListType.ALPHABETICAL_BY_NAME
                        ),
                        artists = api.getArtists()
                    )
                },
            queueAudioSourceManager
                .playingSource()
                .flatMapLatest { source ->
                    playStateStore
                        .isPlaying()
                        .map { isPlaying ->
                            source.takeIf { isPlaying }
                        }
                }
        ) { library, playingSource ->
            val serverId = apiProvider.requireApiId()
            val api = apiProvider.getApi()
            return@combine Library(
                albums = library.albums.map {
                    it.toDomain(isPlaying = playingSource.isAlbumPlaying(serverId, it.id), api)
                },
                artists = library.artists.map {
                    it.toDomain(isPlaying = playingSource.isArtistPlaying(serverId, it.id), api)
                }
            )
        }
    }

    private fun PlayingSource?.isAlbumPlaying(serverId: Long, albumId: String): Boolean {
        if (this == null) {
            return false
        }
        return this.serverId == serverId && this.id == albumId && this.type == PlayingSource.Type.Album
    }

    private fun PlayingSource?.isArtistPlaying(serverId: Long, artistId: String): Boolean {
        if (this == null) {
            return false
        }
        return this.serverId == serverId && this.id == artistId && this.type == PlayingSource.Type.Artist
    }

    private fun Album.toDomain(isPlaying: Boolean, api: SubsonicApi): ru.stresh.youamp.feature.library.domain.Album {
        return ru.stresh.youamp.feature.library.domain.Album(
            id = id,
            title = requireNotNull(name ?: album),
            artist = artist,
            artworkUrl = api.getCoverArtUrl(coverArt),
            isPlaying = isPlaying
        )
    }

    private fun Artist.toDomain(isPlaying: Boolean, api: SubsonicApi): ru.stresh.youamp.feature.library.domain.Artist {
        return ru.stresh.youamp.feature.library.domain.Artist(
            id = id,
            name = name,
            artworkUrl = api.getCoverArtUrl(coverArt),
            isPlaying = isPlaying
        )
    }

    private data class ApiLibrary(
        val albums: List<Album>,
        val artists: List<Artist>
    )
}