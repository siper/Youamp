package ru.stresh.youamp.feature.artists

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.artists.data.ArtistsRepositoryImpl
import ru.stresh.youamp.feature.artists.domain.ArtistsRepository
import ru.stresh.youamp.feature.artists.ui.ArtistsViewModel

val artistsModule = module {
    single<ArtistsRepository> { ArtistsRepositoryImpl(get()) }
    viewModel { ArtistsViewModel(get()) }
}