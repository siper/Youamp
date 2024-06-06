package ru.stersh.youamp.feature.artist.domain

internal interface ArtistInfoRepository {
    suspend fun getArtistInfo(id: String): ArtistInfo
}