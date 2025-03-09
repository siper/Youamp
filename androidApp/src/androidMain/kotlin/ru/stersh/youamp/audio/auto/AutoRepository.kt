package ru.stersh.youamp.audio.auto

interface AutoRepository {
    suspend fun getPlaylists(): List<Auto.Playlist>
    suspend fun getPlaylistSongs(playlistId: String): List<Auto.Song>
    suspend fun getSong(songId: String): Auto.Song
}