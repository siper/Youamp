package ru.stersh.youamp.feature.album.info

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.core.player.Player
import ru.stersh.youamp.feature.album.info.data.AlbumFavoriteRepositoryImpl
import ru.stersh.youamp.feature.album.info.data.AlbumInfoRepositoryImpl
import ru.stersh.youamp.feature.album.info.domain.AlbumFavoriteRepository
import ru.stersh.youamp.feature.album.info.domain.AlbumInfoRepository
import ru.stersh.youamp.feature.album.info.ui.AlbumInfoViewModel
import ru.stersh.youamp.shared.favorites.AlbumFavoritesStorage
import ru.stersh.youamp.shared.queue.PlayerQueueAudioSourceManager

val albumInfoModule =
    module {
        single<AlbumInfoRepository> {
            AlbumInfoRepositoryImpl(
                get<ru.stersh.youamp.core.api.ApiProvider>(),
                get<AlbumFavoritesStorage>(),
            )
        }
        single<AlbumFavoriteRepository> { AlbumFavoriteRepositoryImpl(get<AlbumFavoritesStorage>()) }

        viewModel { (id: String) ->
            AlbumInfoViewModel(
                id,
                get<AlbumInfoRepository>(),
                get<AlbumFavoriteRepository>(),
                get<PlayerQueueAudioSourceManager>(),
                get<Player>(),
            )
        }
    }
