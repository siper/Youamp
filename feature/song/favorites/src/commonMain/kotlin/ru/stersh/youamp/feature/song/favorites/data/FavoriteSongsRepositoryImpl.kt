package ru.stersh.youamp.feature.song.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stersh.youamp.feature.song.favorites.domain.FavoriteSongsRepository
import ru.stersh.youamp.feature.song.favorites.domain.Favorites
import ru.stersh.youamp.feature.song.favorites.domain.Song
import ru.stersh.youamp.shared.favorites.SongFavoritesStorage

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

    private fun ru.stersh.youamp.shared.favorites.Song.toDomain(): Song {
        return Song(
            id = id,
            title = title,
            artist = artist,
            album = album,
            artworkUrl = artworkUrl,
            userRating = userRating
        )
    }
}