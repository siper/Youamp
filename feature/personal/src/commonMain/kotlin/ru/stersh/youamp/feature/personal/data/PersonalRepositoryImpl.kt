package ru.stersh.youamp.feature.personal.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.core.api.ApiProvider
import ru.stersh.youamp.feature.personal.domain.Album
import ru.stersh.youamp.feature.personal.domain.Artist
import ru.stersh.youamp.feature.personal.domain.Personal
import ru.stersh.youamp.feature.personal.domain.PersonalRepository
import ru.stersh.youamp.feature.personal.domain.Playlist
import ru.stersh.youamp.feature.personal.domain.Song
import ru.stersh.youamp.shared.favorites.AlbumFavoritesStorage
import ru.stersh.youamp.shared.favorites.ArtistFavoritesStorage
import ru.stersh.youamp.shared.favorites.SongFavoritesStorage

internal class PersonalRepositoryImpl(
    private val apiProvider: ApiProvider,
    private val songFavoritesStorage: SongFavoritesStorage,
    private val albumFavoritesStorage: AlbumFavoritesStorage,
    private val artistFavoritesStorage: ArtistFavoritesStorage,
) : PersonalRepository {
    override suspend fun getPersonal(): Flow<Personal> {
        return combine(
            apiProvider
                .flowApi()
                .map {
                    it
                        .getPlaylists()
                        .data.playlists.playlist
                        .orEmpty()
                },
            songFavoritesStorage.flowSongs(),
            albumFavoritesStorage.flowAlbums(),
            artistFavoritesStorage.flowArtists(),
        ) { playlists, songs, albums, artists ->
            val playlists =
                playlists
                    .map { playlist ->
                        Playlist(
                            id = playlist.id,
                            name = playlist.name,
                            artworkUrl =
                                apiProvider
                                    .getApi()
                                    .getCoverArtUrl(playlist.coverArt),
                        )
                    }
            val personalSongs =
                songs.map { song ->
                    Song(
                        id = song.id,
                        title = song.title,
                        artist = song.artist,
                        artworkUrl = song.artworkUrl,
                    )
                }
            val personalAlbums =
                albums.map { album ->
                    Album(
                        id = album.id,
                        title = album.title,
                        artist = album.artist,
                        artworkUrl = album.artworkUrl,
                    )
                }
            val personalArtists =
                artists.map { artist ->
                    Artist(
                        id = artist.id,
                        name = artist.name,
                        artworkUrl = artist.artworkUrl,
                    )
                }
            return@combine Personal(
                playlists = playlists,
                songs = personalSongs,
                albums = personalAlbums,
                artists = personalArtists,
            )
        }
    }
}
