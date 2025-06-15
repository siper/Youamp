package ru.stersh.youamp.feature.album.info

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.album.info.data.AlbumFavoriteRepositoryImpl
import ru.stersh.youamp.feature.album.info.data.AlbumInfoRepositoryImpl
import ru.stersh.youamp.feature.album.info.domain.AlbumFavoriteRepository
import ru.stersh.youamp.feature.album.info.domain.AlbumInfoRepository
import ru.stersh.youamp.feature.album.info.ui.AlbumInfoViewModel

val albumInfoModule =
    module {
        single<AlbumInfoRepository> {
            AlbumInfoRepositoryImpl(
                get(),
                get(),
            )
        }
        single<AlbumFavoriteRepository> { AlbumFavoriteRepositoryImpl(get()) }

        viewModel { (id: String) ->
            AlbumInfoViewModel(
                id,
                get(),
                get(),
                get(),
            )
        }
    }
