package ru.stersh.youamp.feature.artist.info.domain

internal interface ArtistInfoRepository {
    suspend fun getArtistInfo(id: String): ArtistInfo
}