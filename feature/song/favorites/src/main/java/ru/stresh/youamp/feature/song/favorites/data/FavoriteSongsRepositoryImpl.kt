package ru.stresh.youamp.feature.song.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stresh.youamp.feature.song.favorites.domain.FavoriteSongsRepository
import ru.stresh.youamp.feature.song.favorites.domain.Favorites
import ru.stresh.youamp.feature.song.favorites.domain.Song
import ru.stresh.youamp.shared.favorites.SongFavoritesStorage

internal class FavoriteSongsRepositoryImpl(
    private val songFavoritesStorage: SongFavoritesStorage
) : FavoriteSongsRepository {

    override fun getFavorites(): Flow<Favorites> {
        return songFavoritesStorage
            .flowSongs()
            .map { favoriteSongs ->
                Favorites(
                    songs = favoriteSongs.map { it.toDomain() }
                )
            }
    }

    private fun ru.stresh.youamp.shared.favorites.Song.toDomain(): Song {
        return Song(
            id = id,
            title = title,
            artist = artist,
            artworkUrl = artworkUrl,
            userRating = userRating
        )
    }
}