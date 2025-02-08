package ru.stresh.youamp.feature.artist.favorites

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.artist.favorites.data.FavoriteArtistsRepositoryImpl
import ru.stresh.youamp.feature.artist.favorites.domain.FavoriteArtistsRepository
import ru.stresh.youamp.feature.artist.favorites.ui.FavoriteArtistViewModel

val artistFavoritesModule = module {
    single<FavoriteArtistsRepository> { FavoriteArtistsRepositoryImpl(get()) }
    viewModel { FavoriteArtistViewModel(get(), get()) }
}