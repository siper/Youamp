package ru.stresh.youamp.feature.artists.domain

internal interface ArtistsRepository {
    suspend fun getArtists(page: Int, pageSize: Int): List<Artist>
}