package ru.stresh.youamp.feature.playlists.domain

internal interface PlaylistsRepository {
    suspend fun getPlaylists(page: Int, pageSize: Int): List<Playlist>
}