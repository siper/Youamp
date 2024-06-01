package ru.stresh.youamp.feature.playlist

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.playlist.ui.PlaylistInfoViewModel

val playlistInfoModule = module {
    viewModel { (id: String) ->
        PlaylistInfoViewModel(id, get(), get(), get(), get(), get())
    }
}