package ru.stresh.youamp.feature.favorite.list

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.favorite.list.data.FavoritesRepositoryImpl
import ru.stresh.youamp.feature.favorite.list.domain.FavoritesRepository
import ru.stresh.youamp.feature.favorite.list.ui.FavoriteListViewModel

val favoriteListModule = module {
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
    viewModel { FavoriteListViewModel(get()) }
}