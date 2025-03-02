package ru.stersh.youamp.feature.artist.info

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.stersh.youamp.feature.artist.info.data.ArtistFavoriteRepositoryImpl
import ru.stersh.youamp.feature.artist.info.data.ArtistInfoRepositoryImpl
import ru.stersh.youamp.feature.artist.info.domain.ArtistFavoriteRepository
import ru.stersh.youamp.feature.artist.info.domain.ArtistInfoRepository
import ru.stersh.youamp.feature.artist.info.ui.ArtistInfoViewModel

val artistInfoModule = module {
    single<ArtistInfoRepository> { ArtistInfoRepositoryImpl(get(), get()) }
    single<ArtistFavoriteRepository> { ArtistFavoriteRepositoryImpl(get()) }
    viewModel { (id: String) ->
        ArtistInfoViewModel(id, get(), get(), get())
    }
}