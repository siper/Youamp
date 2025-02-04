package ru.stersh.youamp.feature.personal.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.core.api.provider.ApiProvider
import ru.stersh.youamp.feature.personal.domain.Album
import ru.stersh.youamp.feature.personal.domain.Artist
import ru.stersh.youamp.feature.personal.domain.Personal
import ru.stersh.youamp.feature.personal.domain.PersonalRepository
import ru.stersh.youamp.feature.personal.domain.Playlist
import ru.stersh.youamp.feature.personal.domain.Song
import ru.stersh.youamp.shared.player.queue.PlayerQueueAudioSourceManager
import ru.stersh.youamp.shared.player.queue.PlayingSource
import ru.stersh.youamp.shared.player.state.PlayStateStore
import ru.stresh.youamp.shared.favorites.AlbumFavoritesStorage
import ru.stresh.youamp.shared.favorites.ArtistFavoritesStorage
import ru.stresh.youamp.shared.favorites.SongFavoritesStorage

internal class PersonalRepositoryImpl(
    private val apiProvider: ApiProvider,
    private val queueAudioSourceManager: PlayerQueueAudioSourceManager,
    private val playStateStore: PlayStateStore,
    private val songFavoritesStorage: SongFavoritesStorage,
    private val albumFavoritesStorage: AlbumFavoritesStorage,
    private val artistFavoritesStorage: ArtistFavoritesStorage
) : PersonalRepository {

    override suspend fun getPersonal(): Flow<Personal> {
        return combine(
            apiProvider.flowApi(),
            queueAudioSourceManager
                .playingSource()
                .flatMapLatest { source ->
                    playStateStore
                        .isPlaying()
                        .map { isPlaying ->
                            source.takeIf { isPlaying }
                        }
                },
            songFavoritesStorage.flowSongs(),
            albumFavoritesStorage.flowAlbums(),
            artistFavoritesStorage.flowArtists()
        ) { api, playingSource, songs, albums, artists ->
            val serverId = requireNotNull(apiProvider.getApiId())
            val playlists = api
                .getPlaylists()
                .map { playlist ->
                    Playlist(
                        id = playlist.id,
                        name = playlist.name,
                        artworkUrl = apiProvider
                            .getApi()
                            .getCoverArtUrl(playlist.coverArt),
                        isPlaying = playingSource?.isPlaylistPlaying(serverId, playlist.id) == true
                    )
                }
            val personalSongs = songs.map { song ->
                    Song(
                        id = song.id,
                        title = song.title,
                        artist = song.artist,
                        artworkUrl = song.artworkUrl,
                        isPlaying = playingSource?.isSongPlaying(serverId, song.id) == true
                    )
                }
            val personalAlbums = albums.map { album ->
                    Album(
                        id = album.id,
                        title = album.title,
                        artist = album.artist,
                        artworkUrl = album.artworkUrl,
                        isPlaying = playingSource?.isAlbumPlaying(serverId, album.id) == true
                    )
                }
            val personalArtists = artists.map { artist ->
                    Artist(
                        id = artist.id,
                        name = artist.name,
                        artworkUrl = artist.artworkUrl,
                        isPlaying = playingSource?.isArtistPlaying(serverId, artist.id) == true
                    )
                }
            return@combine Personal(
                playlists = playlists,
                songs = personalSongs,
                albums = personalAlbums,
                artists = personalArtists
            )
        }
    }

    private fun PlayingSource?.isPlaylistPlaying(serverId: Long, playlistId: String): Boolean {
        if (this == null) {
            return false
        }
        return this.serverId == serverId && this.id == playlistId && this.type == PlayingSource.Type.Playlist
    }

    private fun PlayingSource?.isAlbumPlaying(serverId: Long, albumId: String): Boolean {
        if (this == null) {
            return false
        }
        return this.serverId == serverId && this.id == albumId && this.type == PlayingSource.Type.Album
    }

    private fun PlayingSource?.isSongPlaying(serverId: Long, songId: String): Boolean {
        if (this == null) {
            return false
        }
        return this.serverId == serverId && this.id == songId && this.type == PlayingSource.Type.Song
    }

    private fun PlayingSource?.isArtistPlaying(serverId: Long, artistId: String): Boolean {
        if (this == null) {
            return false
        }
        return this.serverId == serverId && this.id == artistId && this.type == PlayingSource.Type.Artist
    }
}