package ru.stersh.youamp.feature.album.favorites

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.feature.album.favorites.data.FavoriteAlbumsRepositoryImpl
import ru.stersh.youamp.feature.album.favorites.domain.FavoriteAlbumsRepository
import ru.stersh.youamp.feature.album.favorites.ui.FavoriteAlbumsViewModel
import ru.stersh.youamp.shared.favorites.AlbumFavoritesStorage
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager

val albumFavoritesModule =
    module {
        single<FavoriteAlbumsRepository> { FavoriteAlbumsRepositoryImpl(get<AlbumFavoritesStorage>()) }
        viewModel {
            FavoriteAlbumsViewModel(
                get<FavoriteAlbumsRepository>(),
                get<PlayerQueueAudioSourceManager>(),
                get<Player>(),
            )
        }
    }
