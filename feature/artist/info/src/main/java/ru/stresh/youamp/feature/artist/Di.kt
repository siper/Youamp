package ru.stresh.youamp.feature.artist

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.artist.ui.ArtistInfoViewModel

val artistInfoModule = module {
    viewModel { (id: String) ->
        ArtistInfoViewModel(id, get(), get())
    }
}