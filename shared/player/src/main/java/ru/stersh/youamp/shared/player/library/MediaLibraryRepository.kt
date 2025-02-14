package ru.stersh.youamp.shared.player.library

interface MediaLibraryRepository {
    suspend fun getPlaylists(): List<MediaLibrary.Playlist>
    suspend fun getPlaylistSongs(playlistId: String): List<MediaLibrary.Song>
    suspend fun getSong(songId: String): MediaLibrary.Song
}