package ru.stresh.youamp.feature.favorite.list

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.favorite.list.data.FavoriteSongsRepositoryImpl
import ru.stresh.youamp.feature.favorite.list.domain.FavoriteSongsRepository
import ru.stresh.youamp.feature.favorite.list.ui.FavoriteSongsViewModel

val favoriteListModule = module {
    single<FavoriteSongsRepository> { FavoriteSongsRepositoryImpl(get()) }
    viewModel { FavoriteSongsViewModel(get(), get()) }
}