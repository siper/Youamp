package ru.stersh.youamp.feature.artists

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.artists.data.ArtistsRepositoryImpl
import ru.stersh.youamp.feature.artists.domain.ArtistsRepository
import ru.stersh.youamp.feature.artists.ui.ArtistsViewModel

val artistsModule = module {
    single<ArtistsRepository> { ArtistsRepositoryImpl(get()) }
    viewModel { ArtistsViewModel(get()) }
}