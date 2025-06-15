package ru.stersh.youamp.feature.library.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.stersh.subsonic.api.SubsonicApi
import ru.stersh.subsonic.api.model.Album
import ru.stersh.subsonic.api.model.Artist
import ru.stersh.subsonic.api.model.ListType
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.feature.library.domain.Library
import ru.stersh.youamp.feature.library.domain.LibraryRepository
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.queue.PlayingSource

internal class LibraryRepositoryImpl(
    private val apiProvider: ApiProvider,
    private val queueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val player: Player,
) : LibraryRepository {
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override fun getLibrary(): Flow<Library> {
        return combine(
            apiProvider
                .flowApi()
                .map { api ->
                    ApiLibrary(
                        albums =
                            api
                                .getAlbumList2(
                                    type = ListType.ALPHABETICAL_BY_NAME,
                                ).data
                                .albumList2
                                .album
                                .orEmpty(),
                        artists =
                            api
                                .getArtists()
                                .data
                                .artists
                                .index
                                .flatMap { it.artist },
                    )
                },
            queueAudioSourceManager
                .playingSource()
                .flatMapLatest { source ->
                    player
                        .getIsPlaying()
                        .map { isPlaying ->
                            source.takeIf { isPlaying }
                        }
                },
        ) { library, playingSource ->
            val serverId = apiProvider.requireApiId()
            val api = apiProvider.getApi()
            return@combine Library(
                albums =
                    library.albums.map {
                        it.toDomain(
                            isPlaying =
                                playingSource.isAlbumPlaying(
                                    serverId,
                                    it.id,
                                ),
                            api,
                        )
                    },
                artists =
                    library.artists.map {
                        it.toDomain(
                            isPlaying =
                                playingSource.isArtistPlaying(
                                    serverId,
                                    it.id,
                                ),
                            api,
                        )
                    },
            )
        }
    }

    private fun PlayingSource?.isAlbumPlaying(
        serverId: Long,
        albumId: String,
    ): Boolean {
        if (this == null) {
            return false
        }
        return this.serverId == serverId &&
            this.id == albumId &&
            this.type == PlayingSource.Type.Album
    }

    private fun PlayingSource?.isArtistPlaying(
        serverId: Long,
        artistId: String,
    ): Boolean {
        if (this == null) {
            return false
        }
        return this.serverId == serverId &&
            this.id == artistId &&
            this.type == PlayingSource.Type.Artist
    }

    private fun Album.toDomain(
        isPlaying: Boolean,
        api: SubsonicApi,
    ): ru.stersh.youamp.feature.library.domain.Album =
        ru.stersh.youamp.feature.library.domain.Album(
            id = id,
            title = requireNotNull(name ?: album),
            artist = artist,
            artworkUrl = api.getCoverArtUrl(coverArt),
            isPlaying = isPlaying,
        )

    private fun Artist.toDomain(
        isPlaying: Boolean,
        api: SubsonicApi,
    ): ru.stersh.youamp.feature.library.domain.Artist =
        ru.stersh.youamp.feature.library.domain.Artist(
            id = id,
            name = name,
            artworkUrl = api.getCoverArtUrl(coverArt),
            isPlaying = isPlaying,
        )

    private data class ApiLibrary(
        val albums: List<Album>,
        val artists: List<Artist>,
    )
}
