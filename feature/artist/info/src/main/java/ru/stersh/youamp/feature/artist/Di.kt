package ru.stersh.youamp.feature.artist

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.artist.data.ArtistFavoriteRepositoryImpl
import ru.stersh.youamp.feature.artist.data.ArtistInfoRepositoryImpl
import ru.stersh.youamp.feature.artist.domain.ArtistFavoriteRepository
import ru.stersh.youamp.feature.artist.domain.ArtistInfoRepository
import ru.stersh.youamp.feature.artist.ui.ArtistInfoViewModel

val artistInfoModule = module {
    single<ArtistInfoRepository> { ArtistInfoRepositoryImpl(get(), get()) }
    single<ArtistFavoriteRepository> { ArtistFavoriteRepositoryImpl(get()) }
    viewModel { (id: String) ->
        ArtistInfoViewModel(id, get(), get(), get())
    }
}