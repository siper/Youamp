package ru.stersh.youamp.feature.artists

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.artists.data.ArtistsRepositoryImpl
import ru.stersh.youamp.feature.artists.domain.ArtistsRepository
import ru.stersh.youamp.feature.artists.ui.ArtistsViewModel

val artistListModule = module {
    single<ArtistsRepository> { ArtistsRepositoryImpl(get()) }
    viewModel { ArtistsViewModel(get()) }
}