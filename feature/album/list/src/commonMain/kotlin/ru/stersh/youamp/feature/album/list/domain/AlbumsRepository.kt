package ru.stersh.youamp.feature.album.list.domain

internal interface AlbumsRepository {
    suspend fun getAlbums(page: Int, pageSize: Int): List<Album>
}