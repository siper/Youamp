package ru.stersh.youamp.feature.album.info.domain

internal interface AlbumInfoRepository {
    suspend fun getAlbumInfo(id: String): AlbumInfo
}
