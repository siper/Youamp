package ru.stresh.youamp.feature.song.favorites

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.song.favorites.data.FavoriteSongsRepositoryImpl
import ru.stresh.youamp.feature.song.favorites.domain.FavoriteSongsRepository
import ru.stresh.youamp.feature.song.favorites.ui.FavoriteSongsViewModel

val songFavoritesModule = module {
    single<FavoriteSongsRepository> { FavoriteSongsRepositoryImpl(get()) }
    viewModel { FavoriteSongsViewModel(get(), get()) }
}