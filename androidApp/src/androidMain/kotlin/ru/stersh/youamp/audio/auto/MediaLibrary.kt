package ru.stersh.youamp.audio.auto

internal object MediaLibrary {
    const val LIBRARY_ROOT_ID = "[ROOT_ID]"
    const val LIBRARY_PLAYLISTS_ID = "[PLAYLISTS_ID]"
    const val LIBRARY_PLAYLIST_PREFIX = "playlist_"

    fun clearPlaylistId(id: String): String {
        return id.replace(LIBRARY_PLAYLIST_PREFIX, "")
    }

    fun isPlaylist(id: String): Boolean {
        return id.startsWith(LIBRARY_PLAYLIST_PREFIX)
    }
}