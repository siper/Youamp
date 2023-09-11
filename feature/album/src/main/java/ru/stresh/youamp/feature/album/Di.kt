package ru.stresh.youamp.feature.album

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.stresh.youamp.feature.album.ui.AlbumInfoViewModel

val albumInfoModule = module {
    viewModel { (id: String) ->
        AlbumInfoViewModel(id, get(), get())
    }
}