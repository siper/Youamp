package ru.stresh.youamp.feature.album.favorites

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.album.favorites.data.FavoriteAlbumsRepositoryImpl
import ru.stresh.youamp.feature.album.favorites.domain.FavoriteAlbumsRepository
import ru.stresh.youamp.feature.album.favorites.ui.FavoriteAlbumsViewModel

val albumFavoritesModule = module {
    single<FavoriteAlbumsRepository> { FavoriteAlbumsRepositoryImpl(get()) }
    viewModel { FavoriteAlbumsViewModel(get(), get()) }
}