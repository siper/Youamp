package ru.stersh.youamp.feature.artist.list

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.artist.list.data.ArtistsRepositoryImpl
import ru.stersh.youamp.feature.artist.list.domain.ArtistsRepository
import ru.stersh.youamp.feature.artist.list.ui.ArtistsViewModel

val artistListModule = module {
    single<ArtistsRepository> { ArtistsRepositoryImpl(get()) }
    viewModel { ArtistsViewModel(get()) }
}