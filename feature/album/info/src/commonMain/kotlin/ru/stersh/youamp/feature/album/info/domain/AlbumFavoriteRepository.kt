package ru.stersh.youamp.feature.album.info.domain

internal interface AlbumFavoriteRepository {
    suspend fun setFavorite(
        id: String,
        isFavorite: Boolean,
    )
}
