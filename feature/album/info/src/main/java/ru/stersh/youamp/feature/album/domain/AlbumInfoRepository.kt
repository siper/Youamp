package ru.stersh.youamp.feature.album.domain

internal interface AlbumInfoRepository {
    suspend fun getAlbumInfo(id: String): AlbumInfo
}