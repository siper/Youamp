package ru.stresh.youamp.feature.albums.domain

internal interface AlbumsRepository {
    suspend fun getAlbums(page: Int, pageSize: Int): List<Album>
}