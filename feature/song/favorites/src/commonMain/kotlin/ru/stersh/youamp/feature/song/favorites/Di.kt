package ru.stersh.youamp.feature.song.favorites

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.feature.song.favorites.data.FavoriteSongsRepositoryImpl
import ru.stersh.youamp.feature.song.favorites.domain.FavoriteSongsRepository
import ru.stersh.youamp.feature.song.favorites.ui.FavoriteSongsViewModel
import ru.stersh.youamp.shared.favorites.SongFavoritesStorage
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager

val songFavoritesModule =
    module {
        single<FavoriteSongsRepository> { FavoriteSongsRepositoryImpl(get<SongFavoritesStorage>()) }
        viewModel {
            FavoriteSongsViewModel(
                get<FavoriteSongsRepository>(),
                get<PlayerQueueAudioSourceManager>(),
                get<Player>(),
            )
        }
    }
