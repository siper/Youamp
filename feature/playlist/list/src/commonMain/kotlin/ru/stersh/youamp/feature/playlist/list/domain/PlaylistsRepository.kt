package ru.stersh.youamp.feature.playlist.list.domain

import kotlinx.coroutines.flow.Flow

internal interface PlaylistsRepository {
    suspend fun getPlaylists(): Flow<List<Playlist>>
}